package com.brainsmash.broken_world.registry;

import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.brainsmash.broken_world.Main.MODID;
import static com.brainsmash.broken_world.registry.BlockRegister.blocknames;
import static com.brainsmash.broken_world.registry.BlockRegister.blocks;

public class DimensionRegister {

    public static final java.util.List<String> noAirDimension = Arrays.asList("broken_world.moon_type");
    public static final List<String> noCloudDimension = Arrays.asList("broken_world.moon_type");
    public static ConcurrentHashMap<String, PortalLink> dimensions = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Double> dimensionGravity = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Integer> dimensionEnergyCost = new ConcurrentHashMap<>();
    public static void RegistDimension(){
        CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[4]).destDimID(new Identifier("minecraft","overworld")).tintColor(Color.BLUE.getRGB()).registerPortal();
        CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[9]).destDimID(new Identifier(MODID,"moon")).tintColor(Color.WHITE.getRGB()).registerPortal();
        CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[10]).destDimID(new Identifier(MODID,"metallic")).tintColor(Color.GRAY.getRGB()).registerPortal();
        CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[12]).destDimID(new Identifier(MODID,"lush")).tintColor(Color.GREEN.getRGB()).registerPortal();
        CustomPortalBuilder.beginPortal().onlyLightInOverworld().frameBlock(blocks[15]).destDimID(new Identifier(MODID,"sulfuric")).tintColor(new Color(210,180,0).getRGB()).registerPortal();

        dimensions.put("broken_world:moon",new PortalLink(new Identifier(MODID,blocknames[9]),new Identifier(MODID,"moon"),Color.WHITE.getRGB()));
        dimensions.put("broken_world:metallic",new PortalLink(new Identifier(MODID,blocknames[10]),new Identifier(MODID,"metallic"),Color.GRAY.getRGB()));
        dimensions.put("broken_world:lush",new PortalLink(new Identifier(MODID,blocknames[12]),new Identifier(MODID,"lush"),Color.GREEN.getRGB()));
        dimensions.put("broken_world:sulfuric",new PortalLink(new Identifier(MODID,blocknames[15]),new Identifier(MODID,"sulfuric"),new Color(210,180,0).getRGB()));

        dimensionGravity.put("broken_world.moon_type",0.1);
        dimensionGravity.put("broken_world.metallic_type",0.8);
        dimensionGravity.put("broken_world.sulfuric",1.1);

        dimensionEnergyCost.put("broken_world:lush",50000);
        dimensionEnergyCost.put("broken_world:moon",100000);
        dimensionEnergyCost.put("broken_world:metallic",300000);
        dimensionEnergyCost.put("broken_world:sulfuric",300000);

    }
}
