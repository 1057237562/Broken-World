package com.brainsmash.broken_world.util;

public class Vec2i {
    private int x;
    private int y;

    public static final Vec2i POSITIVE_X = new Vec2i(1, 0);
    public static final Vec2i POSITIVE_Y = new Vec2i(0, 1);
    public static final Vec2i NEGATIVE_X = new Vec2i(-1, 0);
    public static final Vec2i NEGATIVE_Y = new Vec2i(0, -1);

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    protected void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i mul(int m) {
        return new Vec2i(getX() * m, getY() * m);
    }

    public static class Mutable extends Vec2i {
        public Mutable(int x, int y) {
            super(x, y);
        }

        public Mutable move(int dx, int dy) {
            set(getX() + dx, getY() + dy);
            return this;
        }

        public Mutable move(Vec2i v) {
            set(getX() + v.getX(), getY() + v.getY());
            return this;
        }
    }
}
