package com.syk;

import java.util.Queue;
import java.util.LinkedList;

public class PlayerPlane extends Thing {
    private int attackTimer;
    private int moveTimer;
    private final int attackSpeed;
    private final int moveSpeed;
    private Queue<String> instr;
    private Game game;

    public PlayerPlane(Position pos, int color, Game game) {
        super(color, 20, 20, pos);
        attackTimer = 0;
        moveTimer = 0;
        attackSpeed = 1;
        moveSpeed = 1;
        this.game = game;
        instr = new LinkedList<String>();
        game.map[pos.posX][pos.posY] = this;
        game.map[pos.posX - 1][pos.posY] = this;
        game.map[pos.posX][pos.posY - 1] = this;
        game.map[pos.posX][pos.posY + 1] = this;

        game.log[(pos.posX)*40 + (pos.posY)] = '_';
        game.log[(pos.posX)*40 + (pos.posY-1)] = '<';
        game.log[(pos.posX)*40 + (pos.posY+1)] = '>';
        game.log[(pos.posX-1)*40 + (pos.posY)] = '^';

        game.logColor[(pos.posX)*40 + (pos.posY)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = (char)this.getColor();
        game.logColor[(pos.posX-1)*40 + (pos.posY)] = (char)this.getColor();
    }

    public boolean move(String instr) {
        boolean ret = false;
        Position next = new Position(pos.posX, pos.posY);
        switch (instr) {
            case "w":
                next.posX--;
                break;
            case "a":
                next.posY--;
                break;
            case "s":
                next.posX++;
                break;
            case "d":
                next.posY++;
                break;
            default:
                break;
        }
        ret = this.moveNextPosition(next);
        return ret;
    }

    private boolean moveNextPosition(Position p) {
        if (p.posX <= 0 || p.posX >= game.map.length || p.posY <= 0 || p.posY >= game.map[0].length - 1) {
            return false;
        }
        if (game.map[p.posX][p.posY] != null && game.map[p.posX][p.posY] != this) {
            return false;
        }
        if (game.map[p.posX][p.posY - 1] != null && game.map[p.posX][p.posY - 1] != this) {
            return false;
        }
        if (game.map[p.posX][p.posY + 1] != null && game.map[p.posX][p.posY + 1] != this) {
            return false;
        }
        if (game.map[p.posX - 1][p.posY] != null && game.map[p.posX - 1][p.posY] != this) {
            return false;
        }

        game.map[pos.posX][pos.posY] = null;
        game.map[pos.posX - 1][pos.posY] = null;
        game.map[pos.posX][pos.posY - 1] = null;
        game.map[pos.posX][pos.posY + 1] = null;

        game.map[p.posX][p.posY] = this;
        game.map[p.posX - 1][p.posY] = this;
        game.map[p.posX][p.posY - 1] = this;
        game.map[p.posX][p.posY + 1] = this;

        
        game.log[(pos.posX)*40 + (pos.posY)] = 255;
        game.log[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.log[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.log[(pos.posX-1)*40 + (pos.posY)] = 255;

        game.logColor[(pos.posX)*40 + (pos.posY)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.logColor[(pos.posX-1)*40 + (pos.posY)] = 255;

        this.pos = p;

        
        game.log[(pos.posX)*40 + (pos.posY)] = '_';
        game.log[(pos.posX)*40 + (pos.posY-1)] = '<';
        game.log[(pos.posX)*40 + (pos.posY+1)] = '>';
        game.log[(pos.posX-1)*40 + (pos.posY)] = '^';

        game.logColor[(pos.posX)*40 + (pos.posY)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = (char)this.getColor();
        game.logColor[(pos.posX-1)*40 + (pos.posY)] = (char)this.getColor();
        return true;
    }

    public boolean shoot() {
        if (pos.posX <= 1){
            return false;
        }
        if (game.map[pos.posX-2][pos.posY] != null){
            game.map[pos.posX-2][pos.posY].collision(this);
            return true;
        } else {
            Bullet bullet = new Bullet(new Position(pos.posX-2, pos.posY), this.getColor(), this.game, this);
            game.map[pos.posX-2][pos.posY] = bullet;
            if (game.addBulletToPool(bullet)){
                return true;
            }
            else {
                return false;
            }
        }
    }

    public boolean inputInstr(String instruction){
        return this.instr.offer(instruction);
    }

    @Override
    public void run() {
        String instruction = instr.poll();
        if (instruction == null) {
            return;
        }

        if(moveTimer > 0) {
            moveTimer--;
        }
        if(attackTimer > 0) {
            attackTimer--;
        }

        if (instruction.equals("j")) {
            if(shoot()){
                moveTimer = moveSpeed;
                attackTimer = attackSpeed;
            }
        }
        else {
            if(move(instruction)){
                moveTimer = moveSpeed;
                attackTimer = attackSpeed;
            }
        }
    }

    @Override
    public void collision(Thing x){
        this.hurt(x.getAttack());
        if(this.health < 0){
            this.destroy();
        }
    }
    
    @Override
    public void destroy() {
        game.map[pos.posX][pos.posY] = null;
        game.map[pos.posX - 1][pos.posY] = null;
        game.map[pos.posX][pos.posY - 1] = null;
        game.map[pos.posX][pos.posY + 1] = null;

        game.pushPlayerFromPool(this);

        game.log[(pos.posX)*40 + (pos.posY)] = 255;
        game.log[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.log[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.log[(pos.posX-1)*40 + (pos.posY)] = 255;

        game.logColor[(pos.posX)*40 + (pos.posY)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.logColor[(pos.posX-1)*40 + (pos.posY)] = 255;
    }


}