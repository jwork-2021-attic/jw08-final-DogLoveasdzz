package com.syk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EnemyPlaneTest {
	@Test
	void testCollision() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(5,5), 1, game);

        e.collision(new PlayerPlane(new Position(9,9),2,game));
        assertNotNull(game.map[5][5]);
        e.collision(new PlayerPlane(new Position(11,11),2,game));
        assertNull(game.map[5][5]);
	}

	@Test
	void testDestroy() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(5,5), 1, game);
        e.destroy();
        assertNull(game.map[5][5]);
        assertNull(game.map[6][5]);
        assertNull(game.map[5][6]);
        assertNull(game.map[5][4]);
	}

	@Test
	void testMove() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(56,5), 1, game);
        assertTrue(e.move());
        assertTrue(e.move());
        assertFalse(e.move());
	}

	@Test
	void testShoot() {
        Game game = new Game();
        EnemyPlane e = new EnemyPlane(new Position(5,5), 1, game);
        e.shoot();
        assertEquals(game.map[7][5].getClass(), Bullet.class);
	}
}
