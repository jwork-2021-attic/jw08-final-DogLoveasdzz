package com.syk;

import java.util.ArrayList;
import java.util.Random;

public class Game implements Runnable{
    public Thing[][] map;
    public PlayerPlane[] players;
    public EnemyPlane[] enemies;
    public Bullet[] bullets;

    public String[] instr;
    public ArrayList<String> logs;
    public char[] log;
    public char[] logColor;
    public boolean isStart;
    
    private int bulletNum;
    private int enemyNum;
    private int waves;

    private final int playerPoolSize;
    private final int bulletPoolSize;
    private final int enemyPoolSize;

    Random random = new Random();

    public Game() {
        waves = 1;
        isStart = false;
        playerPoolSize = 4;
        bulletPoolSize = 100;
        enemyPoolSize = 100;
        bulletNum = 0;
        enemyNum = 0;
        bullets = new Bullet[bulletPoolSize];
        enemies = new EnemyPlane[enemyPoolSize];
        players = new PlayerPlane[playerPoolSize];
        for (int i = 0; i < bulletPoolSize; i++) {
            bullets[i] = null;
        }
        for (int i = 0; i < playerPoolSize; i++) {
            players[i] = null;
        }
        for (int i = 0; i < enemyPoolSize; i++) {
            enemies[i] = null;
        }


        map = new Thing[60][40];
        for(int i = 0; i < 60; i++) {
            for (int j = 0; j < 40; j++) {
                map[i][j] = null;
            }
        }

        instr = new String[4];
        log = new char[2400];
        logColor = new char[2400];
        for (int i = 0; i < 240; i++) {
            log[i] = 255;
            logColor[i] = 255;
        }
        logs = new ArrayList<String>();
    }


    public boolean addBulletToPool(Bullet bullet) {
        if (bulletNum == bulletPoolSize) {
            return false;
        }
        else {
            bullets[bulletNum] = bullet;
            bulletNum++;
            return true;
        }
    }

    public int addPlayerToPool() {
        isStart = true;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                for (int x = 59; x >= 0; x--) {
                    if (map[x][38] == null && map[x][37] == null && map[x][39] == null && map[x-1][38] == null) {
                        players[i] = new PlayerPlane(new Position(x, 38), i + 1, this);
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    public boolean addEnemyToPool(EnemyPlane enemy) {
        if(enemyNum == enemyPoolSize) {
            return false;
        }
        else {
            enemies[enemyNum] = enemy;
            enemyNum++;
            return true;
        }
    }

    public boolean pushEnemyFromPool(EnemyPlane e) {
        for(int i = 0; i < enemies.length; i++) {
            if (enemies[i] == e) {
                for(int j = i; j < enemies.length - 1; j++) {
                    enemies[j] = enemies[j+1];
                }
                enemies[enemies.length-1] = null;
                enemyNum--;
                return true;
            }
        }
        return false;
    }

    public boolean pushPlayerFromPool(PlayerPlane e) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == e) {
                players[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean pushBulletFromPool(Bullet e) {
        for(int i = 0; i < bullets.length; i++) {
            if (bullets[i] == e) {
                for(int j = i; j < bullets.length - 1; j++) {
                    bullets[j] = bullets[j+1];
                }
                bullets[bullets.length-1] = null;
                bulletNum--;
                return true;
            }
        }
        return false;
    }

    public boolean isEnd() {
        if (!isStart) {
            return false;
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                return false;
            }
        }
        return true;
    }

    public void putEnemy(){
        Position[] pos = new Position[5];
        pos[0] = new Position(0, 0);
        pos[1] = new Position(0, 0);
        pos[2] = new Position(0, 0);
        pos[3] = new Position(0, 0);
        pos[4] = new Position(0, 0);
        switch (waves) {
            case 1: {
                pos[0].posX = 2; pos[0].posY = 3;
                pos[1].posX = 2; pos[1].posY = 10;
                pos[2].posX = 2; pos[2].posY = 17;
                pos[3].posX = 2; pos[3].posY = 24;
                pos[4].posX = 2; pos[4].posY = 31;
                waves++;
                break;
            }
            case 2: {
                pos[0].posX = 2; pos[0].posY = 3;
                pos[1].posX = 6; pos[1].posY = 10;
                pos[2].posX = 2; pos[2].posY = 17;
                pos[3].posX = 6; pos[3].posY = 24;
                pos[4].posX = 2; pos[4].posY = 31;
                waves++;
                break;
            }
            case 3: {
                pos[0].posX = 2; pos[0].posY = 3;
                pos[1].posX = 6; pos[1].posY = 10;
                pos[2].posX = 10; pos[2].posY = 17;
                pos[3].posX = 6; pos[3].posY = 24;
                pos[4].posX = 2; pos[4].posY = 31;
                waves = 1;
                break;
            }
        }
        EnemyPlane e1 = new EnemyPlane(pos[0], 5, this);
        EnemyPlane e2 = new EnemyPlane(pos[1], 5, this);
        EnemyPlane e3 = new EnemyPlane(pos[2], 5, this);
        EnemyPlane e4 = new EnemyPlane(pos[3], 5, this);
        EnemyPlane e5 = new EnemyPlane(pos[4], 5, this);
        if(!this.addEnemyToPool(e1)) {
            e1.destroy();
        }
        if(!this.addEnemyToPool(e2)) {
            e2.destroy();
        }
        if(!this.addEnemyToPool(e3)) {
            e3.destroy();
        }
        if(!this.addEnemyToPool(e4)) {
            e4.destroy();
        }
        if(!this.addEnemyToPool(e5)) {
            e5.destroy();
        }
    }

    public boolean isEnemyEmpty(){
        for (int i = 0; i < enemies.length; i++) {
            if(enemies[i] != null) {
                return false;
            }
        }
        return true;
    }

    public String getLog() {
        String ret = "";
        for (int i = 0; i < logs.size(); i++) {
            ret = ret + logs.get(i) + "#";
        }
        return ret;
    }

    @Override
    public void run() {
        while(true) {
            if (isEnemyEmpty()){
                putEnemy();
            }
            for(int i = 0; i < bulletNum; i++) {
                if (bullets[i] != null) {
                    bullets[i].run();
                }
            }
            for(int i = 0; i < enemyNum; i++) {
                if (enemies[i] != null) {
                    enemies[i].run();
                }
            }
            for (int i = 0; i < players.length; i++){
                if(players[i] != null) {
                    players[i].run();
                }
            }
            
            logs.add(new String(log) + "@" + new String(logColor));

            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                e.printStackTrace();;
            }
        }
    }
}