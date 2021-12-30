package com.syk;

import java.util.Random;

public class EnemyPlane extends Thing{
    private int attackTimer;
    private int moveTimer;
    private final int attackSpeed;
    private final int moveSpeed;
    private Game game;
    private int count;

    public EnemyPlane(Position pos, int color, Game game) {
        super(color, 20, 20, pos);
        attackTimer = 0;
        moveTimer = 0;
        attackSpeed = 10;
        moveSpeed = 10;
        count = new Random().nextInt(12);
        this.game = game;
        game.map[pos.posX][pos.posY] = this;
        game.map[pos.posX + 1][pos.posY] = this;
        game.map[pos.posX][pos.posY - 1] = this;
        game.map[pos.posX][pos.posY + 1] = this;

        game.log[(pos.posX)*40 + (pos.posY)] = '_';
        game.log[(pos.posX)*40 + (pos.posY-1)] = '<';
        game.log[(pos.posX)*40 + (pos.posY+1)] = '>';
        game.log[(pos.posX+1)*40 + (pos.posY)] = 'v';
        game.logColor[(pos.posX)*40 + (pos.posY)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = (char)this.getColor();
        game.logColor[(pos.posX+1)*40 + (pos.posY)] = (char)this.getColor();
    }

    public boolean move() {
        if (moveTimer != 0) {
            return false;
        }
        if(pos.posX >= game.map.length-2){
            this.destroy();
            return false;
        }
        if(game.map[pos.posX+2][pos.posY] != null){
            this.collision(game.map[pos.posX+2][pos.posY]);
            game.map[pos.posX+2][pos.posY].collision(this);
            return false;
        }
        if (game.map[pos.posX+1][pos.posY-1] != null){
            this.collision(game.map[pos.posX+1][pos.posY-1]);
            game.map[pos.posX+1][pos.posY-1].collision(this);
            return false;
        }
        if(game.map[pos.posX+1][pos.posY+1] != null) {
            this.collision(game.map[pos.posX+1][pos.posY+1]);
            game.map[pos.posX+1][pos.posY+1].collision(this);
            return false;
        }

        game.log[(pos.posX)*40 + (pos.posY)] = 255;
        game.log[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.log[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.log[(pos.posX+1)*40 + (pos.posY)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.logColor[(pos.posX+1)*40 + (pos.posY)] = 255;

        game.map[pos.posX][pos.posY] = null;
        game.map[pos.posX + 1][pos.posY] = null;
        game.map[pos.posX][pos.posY - 1] = null;
        game.map[pos.posX][pos.posY + 1] = null;

        game.map[pos.posX+1][pos.posY] = this;
        game.map[pos.posX+1][pos.posY-1] = this;
        game.map[pos.posX+1][pos.posY+1] = this;
        game.map[pos.posX+2][pos.posY] = this;
        pos.posX++;

        game.log[(pos.posX)*40 + (pos.posY)] = '_';
        game.log[(pos.posX)*40 + (pos.posY-1)] = '<';
        game.log[(pos.posX)*40 + (pos.posY+1)] = '>';
        game.log[(pos.posX+1)*40 + (pos.posY)] = 'v';
        game.logColor[(pos.posX)*40 + (pos.posY)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = (char)this.getColor();
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = (char)this.getColor();
        game.logColor[(pos.posX+1)*40 + (pos.posY)] = (char)this.getColor();

        return true;
    }

    public boolean shoot() {
        if (attackTimer != 0) {
            return false;
        }
        if (pos.posX >= game.map.length-2){
            return true;
        }
        if (game.map[pos.posX+2][pos.posY] != null){
            game.map[pos.posX+2][pos.posY].hurt(this.getAttack());
            return true;
        } else {
            Bullet bullet = new Bullet(new Position(pos.posX+2, pos.posY), color, game, this);
            game.map[pos.posX+2][pos.posY] = bullet;
            if (game.addBulletToPool(bullet)){
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public void run() {
        if(moveTimer > 0) {
            moveTimer--;
        }
        if(attackTimer > 0) {
            attackTimer--;
        }
        if(count >= 12) {
            count = 0;
        }

        if (count < 6) {
            if(shoot()){
                moveTimer = moveSpeed;
                attackTimer = attackSpeed;
                count++;
            }
        }
        else {
            if(move()){
                moveTimer = moveSpeed;
                attackTimer = attackSpeed;
                count++;
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
        game.map[pos.posX + 1][pos.posY] = null;
        game.map[pos.posX][pos.posY - 1] = null;
        game.map[pos.posX][pos.posY + 1] = null;

        game.log[(pos.posX)*40 + (pos.posY)] = 255;
        game.log[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.log[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.log[(pos.posX+1)*40 + (pos.posY)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY-1)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY+1)] = 255;
        game.logColor[(pos.posX+1)*40 + (pos.posY)] = 255;

        game.pushEnemyFromPool(this);
    }
}