package com.brainsmash.broken_world;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.ItemRegister;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EnumGeneration {

    public static void markStringArray(String[] array, String filename, String pname, String cname) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("package " + pname + ";\n\n");
            writer.write("public enum " + cname + " {\n");
            for (String s : array) {
                writer.write("    " + s.toUpperCase() + ",\n");
            }
            writer.write("}");
        }

    }

    private static final String root = "../../";

    public static void generateEnum() {
        try {
            markStringArray(BlockRegister.blocknames,
                    root + "./src/main/java/com/brainsmash/broken_world/registry/enums/BlockRegistry.java",
                    "com.brainsmash.broken_world.registry.enums", "BlockRegistry");
            markStringArray(ItemRegister.itemnames,
                    root + "./src/main/java/com/brainsmash/broken_world/registry/enums/ItemRegistry.java",
                    "com.brainsmash.broken_world.registry.enums", "ItemRegistry");
            markStringArray(FluidRegister.fluidnames,
                    root + "./src/main/java/com/brainsmash/broken_world/registry/enums/FluidRegistry.java",
                    "com.brainsmash.broken_world.registry.enums", "FluidRegistry");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
