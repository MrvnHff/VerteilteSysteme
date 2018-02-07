package de.htwsaar.vs.server.graph.nodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RobotOrientationTest {

	private RobotOrientation orient1;
	
	@Before
	public void setUp() {
		orient1 = RobotOrientation.NORTH;
	}
	
	@Test
	public void testTurnRight() {
		orient1 = orient1.turnRight();
		assertEquals(RobotOrientation.EAST, orient1);
	}
	
	@Test
	public void testTurnRightFullCircle() {
		for(int i = 0; i < 4; i++) {
			orient1 = orient1.turnRight();
		}
		assertEquals(RobotOrientation.NORTH, orient1);
	}
	
	@Test
	public void testTurnLeft() {
		orient1 = orient1.turnLeft();
		assertEquals(RobotOrientation.WEST, orient1);
	}
	
	@Test
	public void testTurnLeftFullCircle() {
		for(int i = 0; i < 4; i++) {
			orient1 = orient1.turnLeft();
		}
		assertEquals(RobotOrientation.NORTH, orient1);
	}
	
	@Test
	public void testTurnLeftNorthtoSouth() {
		for(int i = 0; i < 2; i++) {
			orient1 = orient1.turnLeft();
		}
		assertEquals(RobotOrientation.SOUTH, orient1);
	}
	
	

}
