package com.brainsmash.broken_world.blocks.entity.electric.base;

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
        // This function can be deleted for optimization because we always start the bfs from the power source and battery in EK algorithm
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
                if(current.getWorld() instanceof ServerWorld sw && !sw.isChunkLoaded(current.getPos().offset(direction,1))){
                    UpdateGraph(current.getWorld(),current.getPos());
                }
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
        if(world.getBlockEntity(pos) instanceof CableBlockEntity current){
            current.ComputeDeltaFlow();
            if (current.deltaFlow != 0 && !(current instanceof PowerBlockEntity || current instanceof BatteryBlockEntity)) {
                bfsQueue.add(current);
            }
        }else {
            for (Direction direction : Direction.values()) {
                if (world.getBlockEntity(pos.offset(direction)) instanceof CableBlockEntity cable) {
                    cable.edges.put(direction.getOpposite(), 0);
                    cable.ComputeDeltaFlow();
                    if (cable.deltaFlow != 0 && !(cable instanceof PowerBlockEntity || cable instanceof BatteryBlockEntity)) {
                        bfsQueue.add(cable);
                    }
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

    private static void CheckPower(){
        for(PowerBlockEntity power : powerList){
            if(-power.deltaFlow > power.getEnergy()){ // issue overdrawn (The deltaflow of PowerBlock will always be non-positive
                int flow = - power.deltaFlow - power.getGenerate(); // Reconfigure flow (flow is a positive value)
                for(Direction direction:Direction.values()){
                    if(power.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable){
                        if(!(adjCable instanceof PowerBlockEntity)) {
                            int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(), 0) - power.edges.getOrDefault(direction, 0)) / 2;

                            if (flow > 0) { // Compute powerOverdrawn
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

    private static void CheckBatteryAndConsumer(){
        for(BatteryBlockEntity battery : storageList){
            if(-battery.deltaFlow > battery.getEnergy()){ // issue overdrawn (The deltaflow of Battery must be less than zero to issue overflow
                int flow = -battery.deltaFlow; // Reconfigure flow (flow is a positive value)
                for(Direction direction:Direction.values()){
                    if(battery.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable) {
                        int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(), 0) - battery.edges.getOrDefault(direction, 0)) / 2;

                        if (flow > 0) { // Compute powerOverflow
                            if (relflow > 0) {
                                int alterflow = Math.min(relflow, flow);
                                battery.edges.compute(direction, (direction1, integer) -> integer + alterflow);
                                adjCable.edges.compute(direction.getOpposite(), (direction1, integer) -> integer - alterflow);
                                adjCable.ComputeDeltaFlow();
                                System.out.println("Battery:"+adjCable.deltaFlow);
                                flow -= alterflow;
                                battery.deltaFlow += alterflow;
                                if (!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)) {
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
            if(battery.deltaFlow > battery.getMaxCapacity() - battery.getEnergy()){
                int flow = battery.deltaFlow - battery.getMaxCapacity() + battery.getEnergy();
                for(Direction direction:Direction.values()){
                    if(battery.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable) {
                        int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(), 0) - battery.edges.getOrDefault(direction, 0)) / 2;

                        if (flow > 0) { // Compute Overflow
                            if (relflow < 0) { // Relative Flow is smaller than zero means flow in
                                int alterflow = -Math.min(-relflow, flow);
                                battery.edges.compute(direction, (direction1, integer) -> integer + alterflow);
                                adjCable.edges.compute(direction.getOpposite(), (direction1, integer) -> integer - alterflow);
                                adjCable.ComputeDeltaFlow();
//                                System.out.println("Consumer:"+adjCable.deltaFlow);
                                flow += alterflow;
                                battery.deltaFlow += alterflow;
                                if (!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)) {
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

        for(ConsumerBlockEntity consumer : consumerList){
            if(consumer.deltaFlow > consumer.getMaxCapacity() - consumer.getEnergy()){
                int flow = consumer.deltaFlow - consumer.getMaxCapacity() + consumer.getEnergy();
                for(Direction direction:Direction.values()){
                    if(consumer.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable) {
                        int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(), 0) - consumer.edges.getOrDefault(direction, 0)) / 2;

                        if (flow > 0) { // Compute Overflow
                            if (relflow < 0) { // Relative Flow is smaller than zero means flow in
                                int alterflow = -Math.min(-relflow, flow);
                                consumer.edges.compute(direction, (direction1, integer) -> integer + alterflow);
                                adjCable.edges.compute(direction.getOpposite(), (direction1, integer) -> integer - alterflow);
                                adjCable.ComputeDeltaFlow();
//                                System.out.println("Consumer:"+adjCable.deltaFlow);
                                flow += alterflow;
                                consumer.deltaFlow += alterflow;
                                if (!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity || adjCable instanceof ConsumerBlockEntity)) {
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

        while(!bfsQueue.isEmpty()) { // Reconfigure the flow
            CableBlockEntity current = bfsQueue.removeFirst();
            if (!(current instanceof PowerBlockEntity || current instanceof BatteryBlockEntity || current instanceof ConsumerBlockEntity)) {
                if (current.deltaFlow != 0) {
                    for (Direction direction : Direction.values()) {
                        if (current.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable) {
                            int relflow = (adjCable.edges.getOrDefault(direction.getOpposite(), 0) - current.edges.getOrDefault(direction, 0)) / 2; // Relative Flow toward target direction
                            if (current.deltaFlow > 0) {
                                if (relflow < 0) { // Energy flow in
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
                            current.markDirty();
                            adjCable.markDirty();
                        }
                        if (current.edges.getOrDefault(direction, 0) > 0) {
                            if (adjCable.visMark != tickMark) {
                                adjCable.visMark = tickMark;
                                adjCable.parent = direction.getOpposite();
                                adjCable.minFlow = Math.min(current.minFlow, current.edges.get(direction));
                                if (adjCable instanceof ConsumerBlockEntity || adjCable instanceof BatteryBlockEntity) {
                                    int flow = Math.min(adjCable.minFlow,adjCable.getMaxCapacity() - adjCable.getEnergy() - adjCable.deltaFlow);
                                    //System.out.println("Flow:"+flow);
                                    if(flow > 0) {
                                        CableBlockEntity ptr = adjCable;
                                        while (!(ptr instanceof PowerBlockEntity)) {
                                            Direction connection = ptr.parent;
                                            ptr.edges.compute(connection, (direction1, integer) -> integer + flow); // Reverse Edge
                                            ptr.deltaFlow += flow;
                                            ptr = (CableBlockEntity) ptr.getAdjacentBlockEntity(connection);
                                            ptr.edges.compute(connection.getOpposite(), (direction1, integer) -> integer - flow); // Add Flow
                                            ptr.deltaFlow -= flow;
                                            ptr.markDirty();
                                        }
                                        bfsQueue.clear();
                                        return;
                                    }
                                }else {
                                    bfsQueue.add(adjCable);
                                }
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
                            current.markDirty();
                            adjCable.markDirty();
                        }
                        if (current.edges.getOrDefault(direction, 0) > 0) {
                            if (adjCable.visMark != tickMark) {
                                adjCable.visMark = tickMark;
                                adjCable.parent = direction.getOpposite();
                                adjCable.minFlow = Math.min(current.minFlow, current.edges.get(direction));
                                if (adjCable instanceof ConsumerBlockEntity) {
                                    int flow = Math.min(adjCable.minFlow,adjCable.getMaxCapacity() - adjCable.getEnergy() - adjCable.deltaFlow);
                                    //System.out.println("Flow:"+flow);
                                    if(flow > 0) {
                                        CableBlockEntity ptr = adjCable;
                                        while (!(ptr instanceof BatteryBlockEntity)) {
                                            Direction connection = ptr.parent;
                                            ptr.edges.compute(connection, (direction1, integer) -> integer + flow); // Reverse Edge
                                            ptr.deltaFlow += flow;
                                            ptr = (CableBlockEntity) ptr.getAdjacentBlockEntity(connection);
                                            ptr.edges.compute(connection.getOpposite(), (direction1, integer) -> integer - flow); // Add Flow
                                            ptr.deltaFlow -= flow;
                                            ptr.markDirty();
                                        }
                                        bfsQueue.clear();
                                        return;
                                    }
                                }else {
                                    bfsQueue.add(adjCable);
                                }
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
            CheckPower();
            CheckBatteryAndConsumer();
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

}
