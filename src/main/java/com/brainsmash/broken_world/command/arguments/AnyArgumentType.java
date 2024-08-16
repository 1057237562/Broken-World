package com.brainsmash.broken_world.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.*;
import java.util.stream.Collectors;

public class AnyArgumentType implements ArgumentType<Object> {
    private static final List<ArgumentType<?>> TYPES = List.of(LongArgumentType.longArg(), DoubleArgumentType.doubleArg(), BoolArgumentType.bool(), StringArgumentType.string());
    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        Object arg = null;
        int start = reader.getCursor();
        for (ArgumentType<?> type : TYPES) {
            try {
                arg = type.parse(reader);
                break;
            } catch (CommandSyntaxException ignored) {
                reader.setCursor(start);
            }
        }
        if (arg == null) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
        }
        return arg;
    }

    public static Object getAny(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Object.class);
    }

    @Override
    public String toString() {
        return "AnyArg()";
    }

    @Override
    public Collection<String> getExamples() {
        Collection<String> c = new HashSet<>();
        return TYPES.stream().flatMap(t -> t.getExamples().stream()).collect(Collectors.toSet());
    }
}