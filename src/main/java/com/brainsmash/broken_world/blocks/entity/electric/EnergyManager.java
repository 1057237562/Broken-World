package com.brainsmash.broken_world.blocks.entity.electric;

import com.mojang.datafixers.types.templates.Check;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EnergyManager {

    private static final Deque<CableBlockEntity> bfsQueue = new ArrayDeque<>();
    private static final List<BatteryBlockEntity> storageList = new ArrayList<>();
    private static final List<ConsumerBlockEntity> consumerList = new ArrayList<>();
    private static final List<CableBlockEntity> cableList = new ArrayList<>();
    private static final List<PowerBlockEntity> powerList = new ArrayList<>();

    private static boolean tickMark = false;

    static {
        ServerTickEvents.START_SERVER_TICK.register(server -> tickMark=!tickMark);
    }

    private static boolean shouldTickCable(CableBlockEntity current) {
        // Make sure we only gather and tick each cable once per tick.
        if (current.tickMark == tickMark) return false;
        // Make sure we ignore cables in non-ticking chunks.
        return current.getWorld() instanceof ServerWorld sw && sw.isChunkLoaded(current.getPos());
    }

    private static void bfs(CableBlockEntity start) {
        if (!shouldTickCable(start)) return;

        bfsQueue.add(start);
        start.tickMark = tickMark;
        if(start instanceof BatteryBlockEntity battery){
            storageList.add(battery);
        }else if(start instanceof ConsumerBlockEntity consumer){
            consumerList.add(consumer);
        }else if(start instanceof PowerBlockEntity power){
            powerList.add(power);
        }else{
            cableList.add(start);
        }

        while (!bfsQueue.isEmpty()) {
            CableBlockEntity current = bfsQueue.removeFirst();

            for (Direction direction : Direction.values()) {
                if (current.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable) {
                    if (shouldTickCable(adjCable)) {
                        if(adjCable instanceof BatteryBlockEntity battery){
                            storageList.add(battery);
                        }else if(adjCable instanceof ConsumerBlockEntity consumer){
                            consumerList.add(consumer);
                        }else if(adjCable instanceof PowerBlockEntity power) {
                            powerList.add(power);
                        }else{
                            bfsQueue.add(adjCable);
                            cableList.add(adjCable);
                        }
                        adjCable.tickMark = tickMark;
                    }
                }
            }
        }
    }

    public static void UpdateGraph(WorldAccess world, BlockPos pos){
        for(Direction direction : Direction.values()){
            if(world.getBlockEntity(pos.offset(direction)) instanceof CableBlockEntity cable){
                cable.edges.put(direction.getOpposite(),0);
                cable.ComputeDeltaFlow();
                if(!(cable instanceof PowerBlockEntity || cable instanceof BatteryBlockEntity)) {
                    bfsQueue.add(cable);
                }
            }
        }

        while(!bfsQueue.isEmpty()){
            CableBlockEntity current = bfsQueue.removeFirst();
            //System.out.println(current.getPos()+":"+current.deltaFlow);
            for(Direction direction : Direction.values()){
                if(current.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable){
                    int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(),0) - current.edges.getOrDefault(direction,0))/2;
                    if(current.deltaFlow > 0){
                        if(relflow < 0){
                            int alterflow = Math.min(-relflow,current.deltaFlow);
                            current.edges.compute(direction,(direction1, integer) -> integer - alterflow);
                            adjCable.edges.compute(direction.getOpposite(),(direction1, integer) -> integer + alterflow);
                            current.deltaFlow -= alterflow;
                            adjCable.ComputeDeltaFlow();
                            if(!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)){
                                bfsQueue.add(adjCable);
                            }
                        }
                    }else if (current.deltaFlow < 0){
                        if(relflow > 0){
                            int alterflow = Math.min(relflow,-current.deltaFlow);
                            current.edges.compute(direction,(direction1, integer) -> integer + alterflow);
                            adjCable.edges.compute(direction.getOpposite(),(direction1, integer) -> integer - alterflow);
                            current.deltaFlow += alterflow;
                            adjCable.ComputeDeltaFlow();
                            if(adjCable.deltaFlow != 0 && !(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)){
                                bfsQueue.add(adjCable);
                            }
                        }
                    }
                    if(current.deltaFlow == 0){
                        break;
                    }
                }
            }
        }
    }

    private static void CheckOverflow(){
        for(PowerBlockEntity power : powerList){
            if(-power.deltaFlow > power.getEnergy()){ // issue overflow (The deltaflow of PowerBlock will always be non-positive
                int flow = - power.deltaFlow - power.getGenerate(); // Reconfigure flow (flow is a positive value)
                for(Direction direction:Direction.values()){
                    if(power.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable){
                        if(!(adjCable instanceof PowerBlockEntity)) {
                            int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(), 0) - power.edges.getOrDefault(direction, 0)) / 2;

                            if (flow > 0) { // Compute powerOverflow
                                if (relflow > 0) {
                                    int alterflow = Math.min(relflow, flow);
                                    power.edges.compute(direction, (direction1, integer) -> integer + alterflow);
                                    adjCable.edges.compute(direction.getOpposite(), (direction1, integer) -> integer - alterflow);
                                    adjCable.ComputeDeltaFlow();
                                    //System.out.println("Cable:"+adjCable.deltaFlow);
                                    flow -= alterflow;
                                    power.deltaFlow += alterflow;
                                    if(!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)){
                                        bfsQueue.add(adjCable);
                                    }
                                }
                            }
                            if (flow == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        while(!bfsQueue.isEmpty()) { // Reconfigure the flow
            CableBlockEntity current = bfsQueue.removeFirst();
            if (!(current instanceof PowerBlockEntity || current instanceof BatteryBlockEntity || current instanceof ConsumerBlockEntity)) {
                if (current.deltaFlow != 0) {
                    for (Direction direction : Direction.values()) {
                        if (current.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable && !(adjCable instanceof PowerBlockEntity)) {
                            int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(), 0) - current.edges.getOrDefault(direction, 0)) / 2; // Relative Flow toward target direction
                            if (current.deltaFlow > 0) {
                                if (relflow < 0) {
                                    int alterflow = Math.min(-relflow, current.deltaFlow);
                                    current.edges.compute(direction, (direction1, integer) -> integer - alterflow);
                                    adjCable.edges.compute(direction.getOpposite(), (direction1, integer) -> integer + alterflow);
                                    current.deltaFlow -= alterflow;
                                    adjCable.ComputeDeltaFlow();
                                    //System.out.println(adjCable.getPos());
                                    if (!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)) {
                                        bfsQueue.add(adjCable);
                                    }
                                    //System.out.println("Cable:" + adjCable.deltaFlow);
                                }
                            } else if (current.deltaFlow < 0) {
                                if (relflow > 0) { // Energy flow out
                                    int alterflow = Math.min(relflow, -current.deltaFlow);
                                    current.edges.compute(direction, (direction1, integer) -> integer + alterflow);
                                    adjCable.edges.compute(direction.getOpposite(), (direction1, integer) -> integer - alterflow);
                                    current.deltaFlow += alterflow;
                                    adjCable.ComputeDeltaFlow();
                                    //System.out.println(adjCable.getPos());
                                    if (!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)) {
                                        bfsQueue.add(adjCable);
                                    }
                                    //System.out.println("Cable:" + adjCable.deltaFlow);
                                }
                            }
                            if (current.deltaFlow == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void EdmondsKarpInPower(){
        for(PowerBlockEntity power : powerList) {
            if(-power.deltaFlow < power.getEnergy()){ // Not in a stable state (Bigger flow expected)
                power.minFlow = power.getEnergy() + power.deltaFlow; // Compute the rest quantity
                bfsQueue.add(power);
            }
        }
        while(!bfsQueue.isEmpty()){
            CableBlockEntity current = bfsQueue.removeFirst();
            if(current.minFlow != 0) {
                for (Direction direction : Direction.values()) {
                    if (current.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable && !(adjCable instanceof PowerBlockEntity)) {
                        if (current.edges.getOrDefault(direction, 0) + adjCable.edges.getOrDefault(direction.getOpposite(), 0) != 2 * (current.getMaxFlow() + adjCable.getMaxFlow())) {
                            current.edges.put(direction, current.getMaxFlow() + adjCable.getMaxFlow());
                            adjCable.edges.put(direction.getOpposite(), current.getMaxFlow() + adjCable.getMaxFlow());
                        }
                        if (current.edges.getOrDefault(direction, 0) > 0) {
                            if (adjCable.visMark != tickMark) {
                                adjCable.visMark = tickMark;
                                adjCable.parent = direction.getOpposite();
                                adjCable.minFlow = Math.min(current.minFlow, current.edges.get(direction));
                                if (adjCable instanceof ConsumerBlockEntity || adjCable instanceof BatteryBlockEntity) {
                                    int flow = adjCable.minFlow;
                                    //System.out.println("Flow:"+flow);
                                    CableBlockEntity ptr = adjCable;
                                    while (!(ptr instanceof PowerBlockEntity)) {
                                        Direction connection = ptr.parent;
                                        ptr.edges.compute(connection, (direction1, integer) -> integer + flow); // Reverse Edge
                                        ptr.deltaFlow += flow;
                                        ptr = (CableBlockEntity) ptr.getAdjacentBlockEntity(connection);
                                        ptr.edges.compute(connection.getOpposite(), (direction1, integer) -> integer - flow); // Add Flow
                                        ptr.deltaFlow -= flow;
                                    }
                                    bfsQueue.clear();
                                    return;
                                }
                                bfsQueue.add(adjCable);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void EdmondsKarpInBattery(){
        for(BatteryBlockEntity battery : storageList) {
            if(-battery.deltaFlow < battery.getEnergy()){ // Not in a stable state (Bigger flow expected)
                battery.minFlow = battery.getEnergy() + battery.deltaFlow; // Compute the rest quantity
                bfsQueue.add(battery);
            }
        }

        while(!bfsQueue.isEmpty()){
            CableBlockEntity current = bfsQueue.removeFirst();
            if(current.minFlow != 0) {
                for (Direction direction : Direction.values()) {
                    if (current.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable && !(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity)) {
                        if (current.edges.getOrDefault(direction, 0) + adjCable.edges.getOrDefault(direction.getOpposite(), 0) != 2 * (current.getMaxFlow() + adjCable.getMaxFlow())) {
                            current.edges.put(direction, current.getMaxFlow() + adjCable.getMaxFlow());
                            adjCable.edges.put(direction.getOpposite(), current.getMaxFlow() + adjCable.getMaxFlow());
                        }
                        if (current.edges.getOrDefault(direction, 0) > 0) {
                            if (adjCable.visMark != tickMark) {
                                adjCable.visMark = tickMark;
                                adjCable.parent = direction.getOpposite();
                                adjCable.minFlow = Math.min(current.minFlow, current.edges.get(direction));
                                if (adjCable instanceof ConsumerBlockEntity) {
                                    int flow = adjCable.minFlow;
                                    //System.out.println("Flow:"+flow);
                                    CableBlockEntity ptr = adjCable;
                                    while (!(ptr instanceof BatteryBlockEntity)) {
                                        Direction connection = ptr.parent;
                                        ptr.edges.compute(connection, (direction1, integer) -> integer + flow); // Reverse Edge
                                        ptr.deltaFlow += flow;
                                        ptr = (CableBlockEntity) ptr.getAdjacentBlockEntity(connection);
                                        ptr.edges.compute(connection.getOpposite(), (direction1, integer) -> integer - flow); // Add Flow
                                        ptr.deltaFlow -= flow;
                                    }
                                    bfsQueue.clear();
                                    return;
                                }
                                bfsQueue.add(adjCable);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void processTick(CableBlockEntity cableBlockEntity) {
        if (!(cableBlockEntity.getWorld() instanceof ServerWorld)) throw new IllegalStateException();

        try {
            bfs(cableBlockEntity);
            if(powerList.size() + cableList.size() + storageList.size() == 0)return;
            CheckOverflow();
            EdmondsKarpInPower();
            EdmondsKarpInBattery();
        } finally {
            cableList.clear();
            storageList.clear();
            consumerList.clear();
            powerList.clear();
            bfsQueue.clear();
        }
    }

    public static long pullEnergy(int energy){
        for(int i = storageList.size()-1;i>=0;i--){
            BatteryBlockEntity battery = storageList.get(i);
            if(Math.min(battery.getMaxFlow(),battery.getEnergy()) >= energy){
                battery.increaseEnergy(-energy);
                energy = 0;
                break;
            }else{
                energy -= Math.min(battery.getMaxFlow(),battery.getEnergy());
                battery.increaseEnergy(-Math.min(battery.getMaxFlow(),battery.getEnergy()));
            }
        }
        return energy;
    }

    public static long pushEnergy(int energy){
        long res = 0;
        for(int i = 0; i < storageList.size();i++){
            BatteryBlockEntity battery = storageList.get(i);
            if(battery.getEnergy() + energy <= battery.getMaxCapacity()){
                battery.increaseEnergy(energy);
                res += energy;
                break;
            }else{
                energy -= battery.getMaxCapacity() - battery.getEnergy();
                res += battery.getMaxCapacity() - battery.getEnergy();
                battery.setEnergy(battery.getMaxCapacity());
            }
        }
        return res;
    }

}
