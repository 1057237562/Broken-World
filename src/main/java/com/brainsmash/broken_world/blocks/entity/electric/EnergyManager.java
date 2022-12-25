package com.brainsmash.broken_world.blocks.entity.electric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;

import java.util.*;

public class EnergyManager {
    private static final List<CableBlockEntity> cableList = new ArrayList<>();
    private static final Deque<CableBlockEntity> bfsQueue = new ArrayDeque<>();
    private static final List<BatteryBlockEntity> storageList = new ArrayList<>();
    private static final List<ConsumerBlockEntity> consumerList = new ArrayList<>();

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

    public static void processTick(CableBlockEntity cableBlockEntity) {
        if (!(cableBlockEntity.getWorld() instanceof ServerWorld)) throw new IllegalStateException();

        try {
            bfs(cableBlockEntity);
            if(cableList.size() + storageList.size() == 0)return;
            storageList.sort((o1, o2) -> (o1.getMaxCapacity() - o1.getEnergy() - o2.getMaxCapacity() + o2.getEnergy()) >= 0 ? 0 : -1);

            // Group all energy into the network.
            int networkCapacity = 0;
            int energyflow = 0;

            for (CableBlockEntity cable : cableList) {
                energyflow += cable.getEnergy();
                networkCapacity += cable.getMaxCapacity();
                //cable.ioBlocked = true;
            }

            // Just in case.
            if (energyflow > networkCapacity) {
                energyflow = networkCapacity;
            }

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
            }
        } finally {
            cableList.clear();
            storageList.clear();
            consumerList.clear();
            bfsQueue.clear();
        }
    }

    public static long pullEnergy(long energy){
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

    public static long pushEnergy(long energy){
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
