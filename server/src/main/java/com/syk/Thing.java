package com.syk;

public abstract class Thing {
    protected int health;
    protected Position pos;
    protected int color;
    protected int attack;

    public Thing(int c, int h, int a, Position p) {
        color = c;
        health = h;
        attack = a;
        pos = p;
    }

    public void hurt(int damage) {
        health -= damage;
    }


    public int getAttack(){
        return attack;
    }

    public int getColor(){
        return color;
    }

    public abstract void run();

    public abstract void collision(Thing x);

    public abstract void destroy();
}