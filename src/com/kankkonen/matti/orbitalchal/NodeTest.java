/**
 * (c) Copyright 2016 Matti Kankkonen
 *
 * for Reaktor Orbital Challenge
 */
package com.kankkonen.matti.orbitalchal;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * @author Matti
 *
 */
public class NodeTest {

	private static final String tst_id = "Test Node";
	private static final double tst_latitude = 65.12345;
	private static final double tst_longitude = 25.12345;
	private static final double tst_altitude = 12.345;
	
	
	/**
	 * Test method for {@link com.kankkonen.matti.orbitalchal.Node#Node(java.lang.String, double, double, double)}.
	 */
	@Test
	public void testNode() {

		Node basicNode = new Node(tst_id, tst_latitude,
				tst_longitude, tst_altitude );

		assertEquals(tst_latitude, basicNode.getLatitude(), 0);
		assertEquals(tst_longitude, basicNode.getLongitude(), 0);
		assertEquals(tst_altitude, basicNode.getAltitude(), 0);
		assertEquals(tst_id, basicNode.getId());
	}

	/**
	 * Test method for {@link com.kankkonen.matti.orbitalchal.Node#getCoverage()}.
	 */
	@Test
	public void testGetCoverage() {
		
		Node node_a = new Node(tst_id, tst_latitude,
				tst_longitude, 300 );
		Node node_b = new Node("B", tst_latitude,
				tst_longitude, 700 );
		Node node_c = new Node("C", tst_latitude,
				tst_longitude, 0 );
		assertEquals(17.2482, node_a.getCoverage(), 1);
		assertEquals(25.7096, node_b.getCoverage(), 1);
		assertEquals(0, node_c.getCoverage(), 0);
	}

	/**
	 * Test method for {@link com.kankkonen.matti.orbitalchal.Node#getDistance(com.kankkonen.matti.orbitalchal.Node)}.
	 */
	@Test
	public void testGetDistance() {
		
		Node node_a = new Node(tst_id, tst_latitude,tst_longitude, 300 );
		Node node_b = new Node("B", 0,tst_longitude, 700 );
		Node node_c = new Node("C", 90,0, 300 );
		Node node_d = new Node("D",-90,0, 300 );
		assertEquals(tst_latitude, node_a.getDistance(node_b), 0);
		assertEquals(180, node_c.getDistance(node_d), 0);

		// Somewhere over the equatorial Pacific Ocean
		Node node_e = new Node("E", 0, 178, 300);
		Node node_f = new Node("F", 0, -178, 700);
		assertEquals(4, node_e.getDistance(node_f), 1);
		
		// Seen any penguins?
		Node node_g = new Node("G", -90, 179, 300);
		Node node_h = new Node("H", -89, -178, 700);
		assertEquals(1, node_g.getDistance(node_h), 1);
		
		Node node_1 = new Node("1", -80,45, 700);
		Node node_2 = new Node("2", -80,-45, 700);
		Node node_3 = new Node("3", -80,135, 700);
		Node node_4 = new Node("4", -80,-135, 700);
		Node node_p = new Node("pole", -90,0, 700);
		assertEquals(10, node_p.getDistance(node_1),1);
		assertEquals(10, node_p.getDistance(node_2),1);
		assertEquals(10, node_p.getDistance(node_3),1);
		assertEquals(10, node_p.getDistance(node_4),1);
		
		node_1 = new Node("1", 90, 1, 300);
		node_2 = new Node("2", -90, -179, 300);
		assertEquals(180, node_1.getDistance(node_2), 0);
		node_1 = new Node("1", 0, 1, 300);
		node_2 = new Node("2", -179, -1, 300);
		assertEquals(178, node_1.getDistance(node_2), 1);
		node_1 = new Node("1", 45, 10, 300);
		node_2 = new Node("2", -10, 0, 300);		
		assertEquals(node_2.getDistance(node_1), node_1.getDistance(node_2), 0);
	}

	/**
	 * Test method for {@link com.kankkonen.matti.orbitalchal.Node#canSee(com.kankkonen.matti.orbitalchal.Node)}.
	 */
	@Test
	public void testCanSee() {
		/* Elevation 300 angle = 17.2482
		 * Elevation 700 angle = 25.7096
		 * 
		 * So two nodes on the same longitude can easily be checked
		 * for visibility overlapping e.g. with elev. 300 the borderline
		 * of the overlapping should be 2 * 17.2482
		 */
		final double start_angle = 90;
		
		Node a = new Node("A", start_angle, 0, 300);
		Node b = new Node("B", start_angle + 2*17.2482, 0, 300);
		Node c = new Node("C", start_angle + 2*17.2482 + 1, 0, 300);
		assertTrue(a.canSee(b));
		assertFalse(a.canSee(c));
		assertTrue(b.canSee(c));
		a = new Node("A", start_angle, 0, 700);
		b = new Node("B", start_angle + 2*25.7096, 0, 700);
		c = new Node("C", start_angle + 2*25.7096 + 1, 0, 700);
		assertTrue(a.canSee(b));
		assertFalse(a.canSee(c));
		assertTrue(b.canSee(c));
	}

	/**
	 * Test method for {@link com.kankkonen.matti.orbitalchal.Node#getNeighbours()}.
	 */
	@Test
	public void testGetNeighbours() {
		Node a = new Node("A", 1,2,3);
		Node b = new Node("B", 2, 3, 4);
		Map<String, Node>nodeMap;
		a.addNeighbor(b);
		assertEquals("A: 1 neighbours:B",a.neighboursToString());
		nodeMap = a.getNeighbours();
		assertEquals(b, nodeMap.get("B"));
	}

}
