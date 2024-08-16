package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.particles.CrackParticle;
import com.brainsmash.broken_world.particles.MagicSpellParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.brainsmash.broken_world.Main.MODID;

public class ParticleRegister {

    public static final DefaultParticleType MAGIC_SPELL_TYPE = FabricParticleTypes.simple(true);
    public static final DefaultParticleType GELOB_TYPE = FabricParticleTypes.simple(true);

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(MODID, "magic_spell"), MAGIC_SPELL_TYPE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(MODID, "gelob"), GELOB_TYPE);
    }

    public static void registerParticleClient() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(
                ((atlasTexture, registry) -> {
                    registry.register(new Identifier(MODID, "particle/magic_spell"));
                    registry.register(new Identifier(MODID, "particle/gelob"));
                }));

        ParticleFactoryRegistry.getInstance().register(MAGIC_SPELL_TYPE, MagicSpellParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(GELOB_TYPE, CrackParticle.Factory::new);
    }
}
