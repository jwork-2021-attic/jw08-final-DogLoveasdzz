package com.syk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GameTest {
	@Test
	void testAddBulletToPool() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(6,6),2,game);
        Bullet b = new Bullet(new Position(1,2),2,game,e);
        game.addBulletToPool(b);
        assertEquals(game.bullets[0], b);
	}

	@Test
	void testAddEnemyToPool() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(6,6),2,game);
        assertTrue(game.addEnemyToPool(e));
        assertEquals(game.enemies[0], e);
	}

	@Test
	void testAddPlayerToPool() {
        Game game = new Game();
        assertEquals(game.addPlayerToPool(), 0);
	}

	@Test
	void testGetLog() {
        Game game = new Game();
        game.logs.add("123");
        game.logs.add("456");
        game.logs.add("789");
        assertEquals(game.getLog(), "123#456#789#");
	}

	@Test
	void testIsEnd() {
        Game game = new Game();
        game.isStart = true;
        assertTrue(game.isEnd());
	}

	@Test
	void testIsEnemyEmpty() {
        Game game = new Game();
        assertTrue(game.isEnemyEmpty());
	}

	@Test
	void testPushBulletFromPool() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(6,6),2,game);
        Bullet b = new Bullet(new Position(1,2),2,game,e);
        game.addBulletToPool(b);
        game.pushBulletFromPool(b);
        assertNull(game.bullets[0]);
        assertFalse(game.pushBulletFromPool(new Bullet(new Position(1,2),2,game,e)));
	}

	@Test
	void testPushEnemyFromPool() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(6,6),2,game);
        game.addEnemyToPool(e);
        game.pushEnemyFromPool(e);
        assertNull(game.enemies[0]);
        assertFalse(game.pushEnemyFromPool(new EnemyPlane(new Position(6,6),2,game)));
	}

	@Test
	void testPushPlayerFromPool() {
        Game game = new Game();
        PlayerPlane e = new PlayerPlane(new Position(6,6),2,game);
        game.addPlayerToPool();
        assertFalse(game.pushPlayerFromPool(e));
        assertNotNull(game.players[0]);
	}

	@Test
	void testPutEnemy() {
        Game game = new Game();
        game.putEnemy();
        assertNotNull(game.enemies[0]);
        assertNotNull(game.enemies[1]);
	}
}
