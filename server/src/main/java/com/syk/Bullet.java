package com.syk;

public class Bullet extends Thing{

    private Game game;
    private Thing owner;
    boolean isPlayer;

    public Bullet(Position pos, int color, Game game, Thing own) {
        super(color, 20, own.getAttack(), pos);
        this.game = game;
        owner = own;
        if(own.getClass() == PlayerPlane.class){
            isPlayer = true;
        }
        else {
            isPlayer = false;
        }
        game.map[pos.posX][pos.posY] = this;

        game.log[(pos.posX)*40 + (pos.posY)] = '*';
        game.logColor[(pos.posX)*40 + (pos.posY)] = (char)this.getColor();
    }

    public boolean move(){
        if(isPlayer) {
            if(pos.posX <= 0) {
                this.destroy();
                return false;
            }
            if(game.map[pos.posX-1][pos.posY] != null) {
                this.collision(game.map[pos.posX-1][pos.posY]);
                game.map[pos.posX-1][pos.posY].collision(this);
                return false;
            }
            game.log[(pos.posX)*40 + (pos.posY)] = 255;
            game.logColor[(pos.posX)*40 + (pos.posY)] = 255;

            game.map[pos.posX][pos.posY] = null;
            game.map[pos.posX-1][pos.posY] = this;
            pos.posX--;
            game.log[(pos.posX)*40 + (pos.posY)] = '*';
            game.logColor[(pos.posX)*40 + (pos.posY)] = (char)this.getColor();
            return true;
        }
        else {
            if(pos.posX >= game.map.length - 1) {
                this.destroy();
                return false;
            }
            if(game.map[pos.posX+1][pos.posY] != null) {
                this.collision(game.map[pos.posX+1][pos.posY]);
                game.map[pos.posX+1][pos.posY].collision(this);
                return false;
            }
            game.log[(pos.posX)*40 + (pos.posY)] = 255;
            game.logColor[(pos.posX)*40 + (pos.posY)] = 255;

            game.map[pos.posX][pos.posY] = null;
            game.map[pos.posX+1][pos.posY] = this;
            pos.posX++;
            game.log[(pos.posX)*40 + (pos.posY)] = '*';
            game.logColor[(pos.posX)*40 + (pos.posY)] = (char)this.getColor();
            return true;
        }
    }

    @Override
    public void run() {
        this.move();
    }

    @Override 
    public void collision(Thing x) {
        this.destroy();
    }
    
    @Override
    public void destroy() {
        game.log[(pos.posX)*40 + (pos.posY)] = 255;
        game.logColor[(pos.posX)*40 + (pos.posY)] = 255;
        game.map[pos.posX][pos.posY] = null;
        game.pushBulletFromPool(this);
    }
}