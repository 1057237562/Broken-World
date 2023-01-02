package com.brainsmash.broken_world.blocks.entity.electric;

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
            System.out.println(current.getPos()+":"+current.deltaFlow);
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
                            if(!(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity)){
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
                            if(adjCable.deltaFlow != 0 && !(adjCable instanceof PowerBlockEntity || adjCable instanceof BatteryBlockEntity)){
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

    private static void EdmondsKarpInPower(){
        for(PowerBlockEntity power : powerList) {
            bfsQueue.add(power);
            power.minFlow = Math.min(power.getEnergy(),power.getMaxFlow());
        }
        while(!bfsQueue.isEmpty()){
            CableBlockEntity current = bfsQueue.removeFirst();
            for(Direction direction : Direction.values()){
                if(current.getAdjacentBlockEntity(direction) instanceof CableBlockEntity adjCable){
                    if(current.edges.getOrDefault(direction,0) + adjCable.edges.getOrDefault(direction.getOpposite(),0) != 2*(current.getMaxFlow() + adjCable.getMaxFlow())){
                        current.edges.put(direction,current.getMaxFlow() + adjCable.getMaxFlow());
                        adjCable.edges.put(direction.getOpposite(),current.getMaxFlow() + adjCable.getMaxFlow());
                    }
                    if(current.edges.getOrDefault(direction,0) > 0) {
                        if (adjCable.visMark != tickMark) {
                            adjCable.visMark = tickMark;
                            adjCable.parent = direction.getOpposite();
                            adjCable.minFlow = Math.min(current.minFlow, current.edges.get(direction));
                            if (adjCable instanceof ConsumerBlockEntity || adjCable instanceof BatteryBlockEntity) {
                                int flow = adjCable.minFlow;
                                System.out.println("Flow:"+flow);
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

    public static void processTick(CableBlockEntity cableBlockEntity) {
        if (!(cableBlockEntity.getWorld() instanceof ServerWorld)) throw new IllegalStateException();

        try {
            bfs(cableBlockEntity);
            if(powerList.size() + cableList.size() + storageList.size() == 0)return;
            EdmondsKarpInPower();
            /*storageList.sort((o1, o2) -> (o1.getMaxCapacity() - o1.getEnergy() - o2.getMaxCapacity() + o2.getEnergy()) >= 0 ? 0 : -1);

            int energyflow = 0;

            for(PowerBlockEntity power : powerList) {
                energyflow += power.getEnergy();
            }

            for (CableBlockEntity cable : cableList) {
                energyflow += cable.getEnergy();
            }

            // Just in case.


            for(ConsumerBlockEntity consumer : consumerList){
                if(consumer.getEnergy() < consumer.getMaxCapacity()) {
                    energyflow -= consumer.getMaxFlow();
                }
            }

            if (energyflow < 0)
                energyflow += pullEnergy(-energyflow);
            else
                energyflow -= pushEnergy(energyflow);

            for(ConsumerBlockEntity consumer : consumerList){
                if(consumer.getEnergy() < consumer.getMaxCapacity()) {
                    consumer.increaseEnergy(consumer.getMaxFlow() + energyflow/consumerList.size());
                }
            }

            int cableCount = cableList.size();
            for (CableBlockEntity cable : cableList) {
                cable.setEnergy(energyflow / cableCount);
                cable.markDirty();
            }*/
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
