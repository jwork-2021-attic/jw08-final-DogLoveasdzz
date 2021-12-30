package com.syk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PlayerPlaneTest {
	@Test
	void testCollision() {
        Game game = new Game();
        PlayerPlane e = new PlayerPlane(new Position(5,5), 1, game);

        e.collision(new EnemyPlane(new Position(9,9),2,game));
        assertNotNull(game.map[5][5]);
        e.collision(new EnemyPlane(new Position(11,11),2,game));
        assertNull(game.map[5][5]);
	}

	@Test
	void testDestroy() {
        Game game = new Game();
        PlayerPlane e = new PlayerPlane(new Position(5,5), 1, game);
        e.destroy();
        assertNull(game.map[5][5]);
        assertNull(game.map[4][5]);
        assertNull(game.map[5][6]);
        assertNull(game.map[5][4]);
	}

	@Test
	void testInputInstr() {
        Game game = new Game();
        PlayerPlane e = new PlayerPlane(new Position(1,1), 1, game);
        assertTrue(e.inputInstr("w"));
	}

	@Test
	void testMove() {
        Game game = new Game();
        PlayerPlane e = new PlayerPlane(new Position(1,1), 1, game);
        assertFalse(e.move("w"));
        assertFalse(e.move("a"));
        assertTrue(e.move("s"));
        assertTrue(e.move("d"));
	}

	@Test
	void testShoot() {
        Game game = new Game();
        PlayerPlane e = new PlayerPlane(new Position(1,1), 1, game);
        assertFalse(e.shoot());
        e.move("s");
        assertTrue(e.shoot());
        assertEquals(game.map[0][1].getClass(), Bullet.class);
	}
}
