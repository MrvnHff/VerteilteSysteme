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
		assertEquals(RobotOrientation.WEST, orient1);
	}
	
	

}
