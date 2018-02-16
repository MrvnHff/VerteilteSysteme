package de.htwsaar.vs.server.graph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RoboGraphTest {

	RoboGraph roboGraph;
	
	@Before
	public void setUp() {
		roboGraph = new RoboGraph(3, 3);
		roboGraph.addRobot("George");
	}
	
	@Test
	public void testGetNeededRotation1() {
		int i = roboGraph.getNeededRotation("George", "0/1");
		assertEquals(1, i);
	}
	
	@Test
	public void testGetNeededRotation2() {
		int i = roboGraph.getNeededRotation("George", "1/0");
		assertEquals(2, i);
	}
	
	@Test
	public void testGetNeededRotation3() {
		int i = roboGraph.getNeededRotation("George", "-1/0");
		assertEquals(0, i);
	}
	
	@Test
	public void testGetNeededRotation4() {
		int i = roboGraph.getNeededRotation("George", "0/-1");
		assertEquals(-1, i);
	}
	
	@Test
	public void testGetNeededRotation5() {
		int i = roboGraph.getNeededRotation("George", "0/0");
		assertEquals(0, i);
	}

}
