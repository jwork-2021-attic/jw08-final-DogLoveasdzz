package com.syk;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BulletTest {
	@Test
	void testCollision() {
        Game game = new Game();
        EnemyPlane owner = new EnemyPlane(new Position(5,5), 1, game);
        Bullet bullet = new Bullet(new Position(1,1),1,game,owner);

        bullet.collision(owner);
        assertNull(game.map[1][1]);

	}

	@Test
	void testMove() {
        Game game = new Game();
        EnemyPlane owner = new EnemyPlane(new Position(5,5), 1, game);
        Bullet bullet = new Bullet(new Position(58,1),1,game,owner);

        assertTrue(bullet.move());
        assertFalse(bullet.move());
	}
}
