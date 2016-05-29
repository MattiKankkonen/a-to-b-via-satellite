/**
 * (c) Copyright 2016 Matti Kankkonen
 *
 * for Reaktor Orbital Challenge
 */
package com.kankkonen.matti.orbitalchal;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * @author Matti
 *
 */
public class NetworkTest {

	/**
	 * Test method for {@link com.kankkonen.matti.orbitalchal.Network#addNode(com.kankkonen.matti.orbitalchal.Node)}.
	 */
	@Test
	public void testAddNode() {
		
		final double start_angle = 90;
		
		Network net = new Network();
		Node a = new Node("A", start_angle, 0, 300);
		Node b = new Node("B", start_angle + 2*17.2482, 0, 300);
		Node c = new Node("C", start_angle + 2*17.2482 + 1, 0, 300);
		assertTrue(a.canSee(b));
		assertFalse(a.canSee(c));
		assertTrue(b.canSee(c));
		net.addNode(a);
		net.addNode(b);
		net.addNode(c);
		net.init();
		List<Node> nodeList = net.getNodes();
		assertEquals(3, nodeList.size());
		Iterator<Node> i = nodeList.iterator();
		while(i.hasNext()) {
			Node node = i.next();
			if(node.getId() == "A") {
				Map<String, Node>map = node.getNeighbours();
				assertEquals(b, map.get("B"));
				assertEquals(null, map.get("C"));
			}
		}
	}

}
