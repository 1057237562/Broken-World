package com.brainsmash.broken_world.registry;

import com.brainsmash.broken_world.command.ConstantKeysSuggestionProvider;
import com.brainsmash.broken_world.command.arguments.AnyArgumentType;
import com.brainsmash.broken_world.command.ConstantsMap;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.*;

public class CommandRegister {

    public static void registerArguments() {
        ArgumentTypeRegistry.registerArgumentType(
                new Identifier("tutorial", "uuid"),
                AnyArgumentType.class, ConstantArgumentSerializer.of(AnyArgumentType::new));
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("constant")
                    .then(argument("key", StringArgumentType.word()).suggests(new ConstantKeysSuggestionProvider())
                            .then(argument("value", new AnyArgumentType())
                                    .executes(context -> {
                                        ConstantsMap.put(StringArgumentType.getString(context, "key"), AnyArgumentType.getAny(context, "value"));
                                        return 1;
                                    }))));

            dispatcher.register(literal("getconstant")
                    .then(argument("key", StringArgumentType.word()).suggests(new ConstantKeysSuggestionProvider())
                            .executes(context -> {
                                String k = StringArgumentType.getString(context, "key");
                                Object v = ConstantsMap.get(k);
                                context.getSource().sendFeedback(Text.literal(v == null ? k + " is null" : k + " = " + v), false);
                                return 1;
                            })));
        });
    }
}
