package com.brainsmash.broken_world.blocks.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;

import java.util.*;

/**
 * Helper class for rendering {@link FluidVariant} instances. Block, Entity, and Screen renders should never call
 * instances of this class directly.
 */
public class FluidVariantRenderer {
    public static final ExpandingVcp VCPS = new ExpandingVcp();
    public static final FluidVariantRenderer INSTANCE = new FluidVariantRenderer();

    /**
     * Renders a list of faces of the given fluid at the specified co-ordinates.
     */
    public void render(FluidVariant fluid, List<FluidRenderFace> faces, VertexConsumerProvider vcp, MatrixStack matrices) {
        Sprite[] sprites = getSprites(fluid);
        RenderLayer layer = getRenderLayer(fluid);

        FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid.getFluid());
        int color = handler.getFluidColor(null, null, fluid.getFluid().getDefaultState());
        renderSimpleFluid(faces, vcp.getBuffer(layer), matrices, sprites[0], sprites[1], color);
    }

    public void renderGuiRectangle(FluidVariant fluid, double x0, double y0, double x1, double y1) {
        List<FluidRenderFace> faces = new ArrayList<>();
        faces.add(FluidRenderFace.createFlatFaceZ(0, 0, 0, x1 - x0, y1 - y0, 0, 1 / 16.0, false, false));

        MatrixStack matrices = new MatrixStack();
        matrices.translate(x0, y0, 0);
        render(fluid, faces, VCPS, matrices);
        VCPS.draw();
    }

    protected static List<FluidRenderFace> splitFaces(List<FluidRenderFace> faces) {
        return FluidFaceSplitter.splitFaces(faces);
    }

    /**
     * Like {@link #splitFaces(List)} but also returns the
     *
     * @param faces
     * @return
     */
    protected static ComponentRenderFaces splitFacesComponent(List<FluidRenderFace> faces) {
        return FluidFaceSplitter.splitFacesComponent(faces);
    }

    protected static RenderLayer getRenderLayer(FluidVariant fluid) {
        final RenderLayer layer;
        Fluid fl = fluid.getFluid();
        if (fl == null) {
            layer = RenderLayer.getTranslucent();
        } else {
            layer = RenderLayers.getFluidLayer(fl.getDefaultState());
        }
        return layer;
    }

    protected static Sprite[] getSprites(FluidVariant fluid) {
        final Sprite still;
        final Sprite flowing;
        Fluid fl = fluid.getFluid();
        MinecraftClient mc = MinecraftClient.getInstance();
        assert fl != null;
        FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fl);
        if (handler != null) {
            Sprite[] sprites = handler.getFluidSprites(null, null, fl.getDefaultState());
            assert sprites.length == 2;
            still = sprites[0];
            flowing = sprites[1];
        } else {
            BlockState state = fl.getDefaultState().getBlockState();
            still = mc.getBlockRenderManager().getModel(state).getParticleSprite();
            flowing = still;
        }

        return new Sprite[]{
                still,
                flowing
        };
    }

    /**
     * @deprecated Use {@link #renderSimpleFluid(List, VertexConsumer, MatrixStack, Sprite, Sprite, int)} instead,
     * which takes both the still and flowing sprites.
     */
    @Deprecated(since = "0.6.0", forRemoval = true)
    protected static void renderSimpleFluid(List<FluidRenderFace> faces, VertexConsumer vc, MatrixStack matrices, Sprite sprite, int colour) {
        renderSimpleFluid(faces, vc, matrices, sprite, sprite, colour);
    }

    /**
     * Renders a fluid in {@link VertexFormats#POSITION_COLOR_TEXTURE}
     */
    protected static void renderSimpleFluid(List<FluidRenderFace> faces, VertexConsumer vc, MatrixStack matrices, Sprite still, Sprite flowing, int colour) {
        int a = (colour >>> 24) & 0xFF;
        int r = (colour >> 16) & 0xFF;
        int g = (colour >> 8) & 0xFF;
        int b = (colour >> 0) & 0xFF;

        Sprite _s = still;
        Sprite _f = flowing;
        for (FluidRenderFace f : splitFaces(faces)) {
            vertex(vc, matrices, f.x0, f.y0, f.z0, f.getU(_s, _f, f.u0), f.getV(_s, _f, f.v0), r, g, b, a, f);
            vertex(vc, matrices, f.x1, f.y1, f.z1, f.getU(_s, _f, f.u1), f.getV(_s, _f, f.v1), r, g, b, a, f);
            vertex(vc, matrices, f.x2, f.y2, f.z2, f.getU(_s, _f, f.u2), f.getV(_s, _f, f.v2), r, g, b, a, f);
            vertex(vc, matrices, f.x3, f.y3, f.z3, f.getU(_s, _f, f.u3), f.getV(_s, _f, f.v3), r, g, b, a, f);
        }
    }

    /**
     * Appends a single vertex in {@link VertexFormats#POSITION_COLOR_TEXTURE_LIGHT_NORMAL} format.
     *
     * @param vc       The {@link VertexConsumer} to append to.
     * @param matrices The {@link MatrixStack} to apply to the x,y,z positions.
     * @param x        Position - X
     * @param y        Position - Y
     * @param z        Position - Z
     * @param u        Texture - U (0 -&gt; 1)
     * @param v        Texture - V (0 -&gt; 1)
     * @param r        Colour - Red (0 -&gt; 255)
     * @param g        Colour - Green (0 -&gt; 255)
     * @param b        Colour - Blue (0 -&gt; 255)
     * @param f        The source for the light and normal.
     */
    protected static void vertex(VertexConsumer vc, MatrixStack matrices, double x, double y, double z, float u, float v, int r, int g, int b, FluidRenderFace f) {
        vertex(vc, matrices, x, y, z, u, v, r, g, b, 0xFF, f);
    }

    /**
     * Appends a single vertex in {@link VertexFormats#POSITION_COLOR_TEXTURE_LIGHT_NORMAL} format.
     *
     * @param vc       The {@link VertexConsumer} to append to.
     * @param matrices The {@link MatrixStack} to apply to the x,y,z positions.
     * @param x        Position - X
     * @param y        Position - Y
     * @param z        Position - Z
     * @param u        Texture - U (0 -&gt; 1)
     * @param v        Texture - V (0 -&gt; 1)
     * @param r        Colour - Red (0 -&gt; 255)
     * @param g        Colour - Green (0 -&gt; 255)
     * @param b        Colour - Blue (0 -&gt; 255)
     * @param a        Colour - Alpha (0 -&gt; 255)
     * @param f        The source for the light and normal.
     */
    protected static void vertex(VertexConsumer vc, MatrixStack matrices, double x, double y, double z, float u, float v, int r, int g, int b, int a, FluidRenderFace f) {
        vertex(vc, matrices, x, y, z, u, v, r, g, b, a, f.light, f.nx, f.ny, f.nz);
    }

    /**
     * Appends a single vertex in {@link VertexFormats#POSITION_COLOR_TEXTURE_LIGHT_NORMAL} format.
     *
     * @param vc       The {@link VertexConsumer} to append to.
     * @param matrices The {@link MatrixStack} to apply to the x,y,z positions.
     * @param x        Position - X
     * @param y        Position - Y
     * @param z        Position - Z
     * @param u        Texture - U (0 -&gt; 1)
     * @param v        Texture - V (0 -&gt; 1)
     * @param r        Colour - Red (0 -&gt; 255)
     * @param g        Colour - Green (0 -&gt; 255)
     * @param b        Colour - Blue (0 -&gt; 255)
     * @param light    Light - packed.
     * @param nx       Normal - X
     * @param ny       Normal - Y
     * @param nz       Normal - Z
     */
    protected static void vertex(VertexConsumer vc, MatrixStack matrices, double x, double y, double z, float u, float v, int r, int g, int b, int light, float nx, float ny, float nz) {
        vertex(vc, matrices, x, y, z, u, v, r, g, b, 0xFF, light, nx, ny, nz);
    }

    /**
     * Appends a single vertex in {@link VertexFormats#POSITION_COLOR_TEXTURE_LIGHT_NORMAL} format.
     *
     * @param vc       The {@link VertexConsumer} to append to.
     * @param matrices The {@link MatrixStack} to apply to the x,y,z positions.
     * @param x        Position - X
     * @param y        Position - Y
     * @param z        Position - Z
     * @param u        Texture - U (0 -&gt; 1)
     * @param v        Texture - V (0 -&gt; 1)
     * @param r        Colour - Red (0 -&gt; 255)
     * @param g        Colour - Green (0 -&gt; 255)
     * @param b        Colour - Blue (0 -&gt; 255)
     * @param a        Colour - Alpha (0 -&gt; 255)
     * @param light    Light - packed.
     * @param nx       Normal - X
     * @param ny       Normal - Y
     * @param nz       Normal - Z
     */
    protected static void vertex(VertexConsumer vc, MatrixStack matrices, double x, double y, double z, float u, float v, int r, int g, int b, int a, int light, float nx, float ny, float nz) {
        vc.vertex(matrices.peek().getPositionMatrix(), (float) x, (float) y, (float) z);
        vc.color(r, g, b, a == 0 ? 0xFF : a);
        vc.texture(u, v);
        vc.overlay(OverlayTexture.DEFAULT_UV);
        vc.light(light);
        vc.normal(matrices.peek().getNormalMatrix(), nx, ny, nz);
        vc.next();
    }

    public static final class ComponentRenderFaces {
        public final List<FluidRenderFace> split, splitExceptTextures;

        public ComponentRenderFaces(List<FluidRenderFace> split, List<FluidRenderFace> splitExceptTextures) {
            this.split = split;
            this.splitExceptTextures = splitExceptTextures;
        }
    }

    /**
     * A simple, auto-expanding {@link VertexConsumerProvider} that can render any number of {@link RenderLayer}'s at
     * once, rather than {@link net.minecraft.client.render.VertexConsumerProvider.Immediate
     * VertexConsumerProvider.Immediate} which can only render the ones provided to it in a map, and 1 other.
     */
    public static final class ExpandingVcp implements VertexConsumerProvider {
        private final List<RenderLayer> before = new ArrayList<>();
        private final List<RenderLayer> solid = new ArrayList<>();
        private final List<RenderLayer> middle = new ArrayList<>();
        private final List<RenderLayer> translucent = new ArrayList<>();
        private final List<RenderLayer> after = new ArrayList<>();

        private final List<BufferBuilder> availableBuffers = new ArrayList<>();
        private final Map<RenderLayer, BufferBuilder> activeBuffers = new HashMap<>();
        private final Set<RenderLayer> knownLayers = new HashSet<>();

        public ExpandingVcp() {
            addLayer(RenderLayer.getSolid(), false);
            addLayer(RenderLayer.getCutout(), true);
            addLayer(RenderLayer.getCutoutMipped(), true);
            addLayer(RenderLayer.getTranslucent(), true);
            addLayer(RenderLayer.getTranslucentNoCrumbling(), true);
            addLayerAfter(RenderLayer.getGlint());
            addLayerAfter(RenderLayer.getEntityGlint());
        }

        public void addLayer(RenderLayer layer, boolean isTranslucent) {
            if (knownLayers.add(layer)) {
                if (isTranslucent) {
                    translucent.add(layer);
                } else {
                    solid.add(layer);
                }
            }
        }

        public void addLayerBefore(RenderLayer layer) {
            if (knownLayers.add(layer)) {
                before.add(layer);
            }
        }

        public void addLayerMiddle(RenderLayer layer) {
            if (knownLayers.add(layer)) {
                middle.add(layer);
            }
        }

        public void addLayerAfter(RenderLayer layer) {
            if (knownLayers.add(layer)) {
                after.add(layer);
            }
        }

        @Override
        public VertexConsumer getBuffer(RenderLayer layer) {
            addLayer(layer, true);
            BufferBuilder buffer = activeBuffers.get(layer);
            if (buffer == null) {
                if (availableBuffers.isEmpty()) {
                    buffer = new BufferBuilder(1 << 12);
                } else {
                    buffer = availableBuffers.remove(availableBuffers.size() - 1);
                }
                activeBuffers.put(layer, buffer);
            }
            if (!buffer.isBuilding()) {
                buffer.begin(layer.getDrawMode(), layer.getVertexFormat());
            }
            return buffer;
        }

        /**
         * Draws every buffer in this VCP, explicitly not using the {@link GraphicsMode#FABULOUS} mode's alternate
         * framebuffer, so this is safe to use in GUIs.
         *
         * @see #drawDirectly()
         */
        public void draw() {
            RenderSystem.runAsFancy(this::drawDirectly);
        }

        /**
         * Directly draws every buffer in this VCP. NOTE: in GUIs this won't work correctly when
         * {@link GraphicsMode#FABULOUS} is used: instead you should use {@link #draw()}.
         */
        public void drawDirectly() {
            draw(before);
            draw(solid);
            draw(middle);
            draw(translucent);
            draw(after);
            assert activeBuffers.isEmpty();
        }

        private void draw(List<RenderLayer> layers) {
            for (RenderLayer layer : layers) {
                BufferBuilder buffer = activeBuffers.remove(layer);
                if (buffer != null) {
                    layer.draw(buffer, 0, 0, 0);
                }
            }
        }
    }

    final class FluidFaceSplitter {

        static List<FluidRenderFace> splitFaces(List<FluidRenderFace> faces) {
            return splitFacesComponent(faces).split;
        }

        static ComponentRenderFaces splitFacesComponent(List<FluidRenderFace> faces) {
            List<FluidRenderFace> splitFull = new ArrayList<>();
            List<FluidRenderFace> splitTex = new ArrayList<>();

            splitFull.addAll(faces);
            splitTex.addAll(faces);

            separateFaces(true, splitFull, splitTex);
            separateFaces(false, splitFull, splitTex);

            // Separates the given faces into distinct renderable faces that all have UV bounds between 0 and 1.
            return new ComponentRenderFaces(splitFull, splitTex);
        }

        private static void separateFaces(boolean u, List<FluidRenderFace> splitFull, List<FluidRenderFace> splitTex) {
            FluidRenderFace[] inputFull = splitFull.toArray(new FluidRenderFace[0]);
            FluidRenderFace[] inputTex = splitTex.toArray(new FluidRenderFace[0]);
            splitFull.clear();
            splitTex.clear();

            for (int i = 0; i < inputFull.length; i++) {
                FluidRenderFace face = inputFull[i];
                FluidRenderFace faceT = inputTex[i];

                // OPTIMISATION!
                // double lowest = q.lowest(u);
                // double highest = q.highest(u);
                // if (!doesCrossTextureBound(lowest, highest)) {
                // splitFull.add(q.toRounded(u));
                // splitTex.add(faceT);
                // }

                new Quad(face, faceT).split(u, splitFull, splitTex);
            }
        }

        private static boolean doesCrossTextureBound(double a, double b) {
            if (a == b) {
                return false;
            }
            if (a > b) {
                double t = a;
                a = b;
                b = t;
            }
            int ra = (int) Math.floor(a);
            int rb = (int) Math.floor(b);
            if (rb == b) {
                // Upper bound
                return rb - 1 != ra;
            }
            return rb == ra;
        }

        static final class Vertex {
            double x, y, z;
            /**
             * U, V rounded co-ords
             */
            double uR, vR;
            /**
             * U, V normal (not rounded) co-ords
             */
            double uN, vN;
            Line l0, l1;

            Vertex() {
            }

            Vertex(double x, double y, double z, double uR, double vR, double uN, double vN) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.uR = uR;
                this.vR = vR;
                this.uN = uN;
                this.vN = vN;
            }

            public Vertex(Vertex from) {
                this(from.x, from.y, from.z, from.uR, from.vR, from.uN, from.vN);
            }

            void set(FluidRenderFace rounded, FluidRenderFace normal, int i) {
                final FluidRenderFace f = rounded;
                final FluidRenderFace n = normal;
                x = i == 0 ? f.x0 : i == 1 ? f.x1 : i == 2 ? f.x2 : f.x3;
                y = i == 0 ? f.y0 : i == 1 ? f.y1 : i == 2 ? f.y2 : f.y3;
                z = i == 0 ? f.z0 : i == 1 ? f.z1 : i == 2 ? f.z2 : f.z3;
                uR = i == 0 ? f.u0 : i == 1 ? f.u1 : i == 2 ? f.u2 : f.u3;
                vR = i == 0 ? f.v0 : i == 1 ? f.v1 : i == 2 ? f.v2 : f.v3;
                uN = i == 0 ? n.u0 : i == 1 ? n.u1 : i == 2 ? n.u2 : n.u3;
                vN = i == 0 ? n.v0 : i == 1 ? n.v1 : i == 2 ? n.v2 : n.v3;
            }

            double texN(boolean _u) {
                return _u ? uN : vN;
            }

            double texR(boolean _u) {
                return _u ? uR : vR;
            }

            @Override
            public String toString() {
                return x + " " + y + " " + z + " " + uN + " " + vN;
            }
        }

        static final class Line {
            final Vertex v0, v1;

            Line(Vertex v0, Vertex v1) {
                this.v0 = v0;
                this.v1 = v1;
                v0.l1 = this;
                v1.l0 = this;
            }
        }

        static final class Quad {
            final Vertex v0, v1, v2, v3;
            final Line l0, l1, l2, l3;
            final int light;
            final float nx, ny, nz;
            final boolean flowing;

            Quad(int light, float nx, float ny, float nz, boolean flowing) {
                this(new Vertex(), new Vertex(), new Vertex(), new Vertex(), light, nx, ny, nz, flowing);
            }

            Quad(FluidRenderFace rounded, FluidRenderFace normal) {
                this(rounded.light, rounded.nx, rounded.ny, rounded.nz, rounded.flowing);
                set(rounded, normal);
            }

            public Quad(Vertex v0, Vertex v1, Vertex v2, Vertex v3, int light, float nx, float ny, float nz, boolean flowing) {
                this.v0 = v0.l0 == null && v0.l1 == null ? v0 : new Vertex(v0);
                this.v1 = v1.l0 == null && v1.l1 == null ? v1 : new Vertex(v1);
                this.v2 = v2.l0 == null && v2.l1 == null ? v2 : new Vertex(v2);
                this.v3 = v3.l0 == null && v3.l1 == null ? v3 : new Vertex(v3);

                l0 = new Line(v0, v1);
                l1 = new Line(v1, v2);
                l2 = new Line(v2, v3);
                l3 = new Line(v3, v0);

                this.light = light;
                this.nx = nx;
                this.ny = ny;
                this.nz = nz;
                this.flowing = flowing;
            }

            void set(FluidRenderFace rounded, FluidRenderFace normal) {
                v0.set(rounded, normal, 0);
                v1.set(rounded, normal, 1);
                v2.set(rounded, normal, 2);
                v3.set(rounded, normal, 3);
            }

            void split(boolean u, List<FluidRenderFace> splitFull, List<FluidRenderFace> splitTex) {
                // double min = lowest(u);
                // double max = highest(u);
                // if (!doesCrossTextureBound(min, max)) {
                // splitFull.add(this.toRounded(u));
                // splitTex.add(nonSplit);
                // return;
                // }

                Vertex lowestVertex = v0;
                Vertex highestVertex = v0;

                Vertex[] searchVertices = new Vertex[]{
                        v1,
                        v2,
                        v3
                };
                double[] searchPoints = new double[]{
                        v1.texN(u),
                        v2.texN(u),
                        v3.texN(u)
                };
                for (int i = 0; i < searchVertices.length; i++) {
                    Vertex v = searchVertices[i];
                    double point = searchPoints[i];
                    if (point < lowestVertex.texN(u)) {
                        lowestVertex = v;
                    }
                    if (point >= highestVertex.texN(u)) {
                        highestVertex = v;
                    }
                }

                if (lowestVertex == highestVertex) {
                    splitFull.add(this.toRounded(u));
                    splitTex.add(this.toFace());
                    return;
                }

                List<Vertex> lowToHigh0 = new ArrayList<>(4);
                List<Vertex> lowToHigh1 = new ArrayList<>(4);

                lowToHigh0.add(lowestVertex);
                lowToHigh1.add(lowestVertex);

                Vertex h = lowestVertex;
                do {
                    h = h.l1.v1;
                    if (h == lowestVertex) {
                        throw new IllegalStateException("h == lowestVertex");
                    }
                    lowToHigh0.add(h);
                } while (h != highestVertex);

                h = lowestVertex;
                do {
                    h = h.l0.v0;
                    if (h == lowestVertex) {
                        throw new IllegalStateException("h == lowestVertex");
                    }

                    lowToHigh1.add(h);
                } while (h != highestVertex);

                List<BucketedVertexList> separated0 = separateVertices(u, lowToHigh0);
                List<BucketedVertexList> separated1 = separateVertices(u, lowToHigh1);
                assert separated0.size() == separated1.size();

                // Now we've got two lists of lists of the texture co-ords, we can create quads from both
                for (int i = 0; i < separated0.size(); i++) {
                    BucketedVertexList bucket0 = separated0.get(i);
                    BucketedVertexList bucket1 = separated1.get(i);
                    assert bucket0.texValue == bucket1.texValue;
                    List<Vertex> list0 = bucket0.list;
                    List<Vertex> list1 = bucket1.list;

                    while (true) {
                        final int size0 = list0.size();
                        final int size1 = list1.size();
                        if (size0 > 1 && size1 > 1) {
                            Vertex vl0 = list0.remove(0);
                            Vertex vl1 = list0.get(0);
                            Vertex vr0 = list1.remove(0);
                            Vertex vr1 = list1.get(0);
                            Quad quad = new Quad(vl0, vl1, vr1, vr0, light, nx, ny, nz, flowing);
                            splitFull.add(quad.toRounded(u, bucket0.texValue));
                            splitTex.add(quad.toFace());
                        } else if (size0 > 2 && size1 > 0) {
                            Vertex vl0 = list0.remove(0);
                            Vertex vl1 = list0.remove(0);
                            Vertex vl2 = list0.get(0);
                            Vertex vr0 = list1.get(0);
                            Quad quad = new Quad(vl0, vl1, vl2, vr0, light, nx, ny, nz, flowing);
                            splitFull.add(quad.toRounded(u, bucket0.texValue));
                            splitTex.add(quad.toFace());
                        } else if (size0 > 0 && size1 > 2) {
                            Vertex vl0 = list0.get(0);
                            Vertex vr0 = list1.remove(0);
                            Vertex vr1 = list1.remove(0);
                            Vertex vr2 = list1.get(0);
                            Quad quad = new Quad(vl0, vr2, vr1, vr0, light, nx, ny, nz, flowing);
                            splitFull.add(quad.toRounded(u, bucket0.texValue));
                            splitTex.add(quad.toFace());
                        } else if (size0 > 1 && size1 > 0) {
                            Vertex vl0 = list0.remove(0);
                            Vertex vl1 = list0.get(0);
                            Vertex vr0 = list1.get(0);
                            Quad quad = new Quad(vl0, vl1, vr0, vr0, light, nx, ny, nz, flowing);
                            splitFull.add(quad.toRounded(u, bucket0.texValue));
                            splitTex.add(quad.toFace());
                        } else if (size0 > 0 && size1 > 1) {
                            Vertex vl0 = list0.get(0);
                            Vertex vr0 = list1.remove(0);
                            Vertex vr1 = list1.get(0);
                            Quad quad = new Quad(vl0, vr1, vr0, vl0, light, nx, ny, nz, flowing);
                            splitFull.add(quad.toRounded(u, bucket0.texValue));
                            splitTex.add(quad.toFace());
                        } else if (size0 > 2) {
                            Vertex vl0 = list0.remove(0);
                            Vertex vl1 = list0.remove(0);
                            Vertex vl2 = list0.get(0);
                            Quad quad = new Quad(vl0, vl1, vl2, vl0, light, nx, ny, nz, flowing);
                            splitFull.add(quad.toRounded(u, bucket0.texValue));
                            splitTex.add(quad.toFace());
                        } else if (size1 > 2) {
                            Vertex vr0 = list1.remove(0);
                            Vertex vr1 = list1.remove(0);
                            Vertex vr2 = list1.get(0);
                            Quad quad = new Quad(vr2, vr1, vr0, vr2, light, nx, ny, nz, flowing);
                            splitFull.add(quad.toRounded(u, bucket0.texValue));
                            splitTex.add(quad.toFace());
                        } else if (size0 + size1 > 2) {
                            throw new IllegalStateException("Unhandled size: [ " + size0 + ", " + size1 + " ]");
                        } else {
                            break;
                        }
                    }
                }
            }

            private static List<BucketedVertexList> separateVertices(boolean u, List<Vertex> in) {
                List<BucketedVertexList> out = new ArrayList<>();
                BucketedVertexList prevList = null;
                for (Vertex v : in) {
                    double tex_d = v.texN(u);
                    int tex_i = (int) Math.floor(tex_d);
                    if (prevList == null) /* First vertex */ {
                        if (tex_d == tex_i) /* Right on the bounds */ {
                            // Add one for the lower ones
                            out.add(prevList = new BucketedVertexList(tex_i - 1));
                            prevList.list.add(v);
                            // Add one for the higher ones
                            out.add(prevList = new BucketedVertexList(tex_i));
                            prevList.list.add(v);
                        } else /* Somewhere inside the bounds */ {
                            out.add(prevList = new BucketedVertexList(tex_i));
                            prevList.list.add(v);
                        }
                    } else {

                        if (prevList.texValue == tex_i) /* We are in the same bound */ {
                            prevList.list.add(v);
                        } else /* We are on a different bound */ {

                            // Generate vertices between the previous one and this one

                            Vertex vPrevious = prevList.list.get(prevList.list.size() - 1);

                            // Generate a single extra vertex to complete the prevList
                            prevList.list.add(interp(u, vPrevious, v, prevList.texValue + 1));

                            for (int i = prevList.texValue + 1; i < tex_i; i++) {
                                // Generate two vertices for every other texture index
                                out.add(prevList = new BucketedVertexList(i));
                                prevList.list.add(interp(u, vPrevious, v, i));
                                prevList.list.add(interp(u, vPrevious, v, i + 1));
                            }

                            if (tex_d == tex_i) /* Right on the bounds */ {
                                // Add a single one for the higher ones
                                out.add(prevList = new BucketedVertexList(tex_i));
                                prevList.list.add(v);
                            } else /* Somewhere on a higher bound */ {
                                // Generate two vertices for every other texture index
                                out.add(prevList = new BucketedVertexList(tex_i));
                                prevList.list.add(interp(u, vPrevious, v, tex_i));
                                prevList.list.add(v);
                            }
                        }
                    }
                }
                return out;
            }

            private static Vertex interp(boolean u, Vertex v0, Vertex v1, int newTextureCoord) {
                double t0 = v0.texN(u);
                double t1 = v1.texN(u);
                if (t0 == newTextureCoord) {
                    return v0;
                } else if (t1 == newTextureCoord) {
                    return v1;
                }
                double interpValue = (newTextureCoord - t0) / (t1 - t0);
                return new Vertex(
                        //
                        interp(v0.x, v1.x, interpValue), //
                        interp(v0.y, v1.y, interpValue), //
                        interp(v0.z, v1.z, interpValue), //
                        interp(v0.uR, v1.uR, interpValue), //
                        interp(v0.vR, v1.vR, interpValue), //
                        interp(v0.uN, v1.uN, interpValue), //
                        interp(v0.vN, v1.vN, interpValue)//
                );
            }

            private static double interp(double a, double b, double interpValue) {
                return a * (1 - interpValue) + b * interpValue;
            }

            static class BucketedVertexList {
                final int texValue;
                final List<Vertex> list = new ArrayList<>();

                public BucketedVertexList(int texValue) {
                    this.texValue = texValue;
                }
            }

            FluidRenderFace toRounded(boolean u) {
                return toRounded(u, (int) Math.floor(lowestR(u)));
            }

            FluidRenderFace toRounded(boolean u, int min) {
                double _u0 = round(u, min, v0.uR);
                double _u1 = round(u, min, v1.uR);
                double _u2 = round(u, min, v2.uR);
                double _u3 = round(u, min, v3.uR);

                double _v0 = round(!u, min, v0.vR);
                double _v1 = round(!u, min, v1.vR);
                double _v2 = round(!u, min, v2.vR);
                double _v3 = round(!u, min, v3.vR);

                return new FluidRenderFace(v0.x, v0.y, v0.z, _u0, _v0, //
                        v1.x, v1.y, v1.z, _u1, _v1, //
                        v2.x, v2.y, v2.z, _u2, _v2, //
                        v3.x, v3.y, v3.z, _u3, _v3, //
                        light, nx, ny, nz, flowing);
            }

            FluidRenderFace toFace() {
                return new FluidRenderFace(v0.x, v0.y, v0.z, v0.uN, v0.vN, //
                        v1.x, v1.y, v1.z, v1.uN, v1.vN, //
                        v2.x, v2.y, v2.z, v2.uN, v2.vN, //
                        v3.x, v3.y, v3.z, v3.uN, v3.vN, //
                        light, nx, ny, nz, flowing);
            }

            private double lowestR(boolean u) {
                return Math.min(Math.min(v0.texR(u), v1.texR(u)), Math.min(v2.texR(u), v3.texR(u)));
            }

            private static double round(boolean should, int min, double value) {
                return should ? value - min : value;
            }

            @Override
            public String toString() {
                return "Quad {\n  " + v0 + "\n  " + v1 + "\n  " + v2 + "\n  " + v3 + "\n}";
            }
        }
    }
}