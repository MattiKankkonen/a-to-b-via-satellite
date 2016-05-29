/**
 * (c) Copyright 2016 Matti Kankkonen
 *
 * for Reaktor Orbital Challenge
 */
package com.kankkonen.matti.orbitalchal;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

/**
 * @author Matti
 *
 */
public class PathFinderTest {

	/**
	 * Test method for {@link com.kankkonen.matti.orbitalchal.PathFinder#findPath()}.
	 */
	@Test
	public void testFindPath() {
		
		Network net = new Network();
		Node node_a = new Node("Helsinki",60.192059,24.945831,0.0);
		Node node_b = new Node("London",51.5085300,-0.1257400,0);
		net.addNode(node_a);
		net.addNode(node_b);
		net.addNode(new Node("Sat 2", 54.3002,25.998,700));
		net.addNode(new Node("Sat 3", 15,24, 700));
		net.addNode(new Node("Sat 4", 0,0, 700));
		net.addNode(new Node("Sat 5", -10, 15, 700));
		net.addNode(new Node("Sat 6", 35, -10, 700));
		net.init();
		Iterator<Node> nodeIter = net.getNodes().iterator();
		while(nodeIter.hasNext()) {
			Node node = nodeIter.next();
			System.out.println(node.neighboursToString());
		}
		PathFinder finder = new PathFinder(node_a, node_b);
		finder.findPath();
		assertEquals(2, finder.getBestPathLength());
	}
	
	@Test
	public void testNoPath() {
		Network net = new Network();
		Node node_a = new Node("Helsinki",60.192059,24.945831,0.0);
		Node node_b = new Node("Tokyo",35.652832,139.6917, 0.0 );
		net.addNode(node_a);
		net.addNode(node_b);
		net.addNode(new Node("SAT1", 50.123, 130.123, 700));
		net.init();
		Iterator<Node> nodeIter = net.getNodes().iterator();
		while(nodeIter.hasNext()) {
			Node node = nodeIter.next();
			System.out.println(node.neighboursToString());
		}
		PathFinder finder = new PathFinder(node_a, node_b);
		finder.findPath();
		assertEquals(0, finder.getBestPathLength());
	}
	
	@Test
	public void testPathTokyo() {
		Network net = new Network();
		Node node_a = new Node("Helsinki",60.192059,24.945831,0.0);
		Node node_b = new Node("Tokyo",35.652832,139.6917, 0.0 );
		net.addNode(node_a);
		net.addNode(node_b);
		net.addNode(new Node("SAT1", 0.123, 130.123, 700));
		net.addNode(new Node("SAT2", 90.314, 60.123, 400));
		net.addNode(new Node("SAT3", 70.123, 30.123, 400));
		net.addNode(new Node("SAT4", 60.123, -175.123, 400));
		net.addNode(new Node("SAT5", 30.123, 175.123, 400));
		net.addNode(new Node("SAT6", -10.123, 160.323, 700));
		net.addNode(new Node("SAT7", 10, 140.323, 700));
		net.init();
		Iterator<Node> nodeIter = net.getNodes().iterator();
		while(nodeIter.hasNext()) {
			Node node = nodeIter.next();
			System.out.println(node.neighboursToString());
		}
		PathFinder finder = new PathFinder(node_a, node_b);
		finder.findPath();
		assertEquals(6, finder.getBestPathLength());
	}
	
	@Test
	public void testPathNewYork() {
		Network net = new Network();
		Node node_a = new Node("Helsinki",60.192059,24.945831,0.0);
		Node node_c = new Node("New York",40.7128, -74.0059, 0.0 );
		net.addNode(node_a);
		net.addNode(node_c);
		net.addNode(new Node("SAT1", 0.123, 130.123, 700));
		net.addNode(new Node("SAT2", 90.314, 60.123, 400));
		net.addNode(new Node("SAT3", 70.123, 30.123, 400));
		net.addNode(new Node("SAT4", 60.123, -175.123, 400));
		net.addNode(new Node("SAT5", 30.123, 175.123, 400));
		net.addNode(new Node("SAT6", -10.123, 160.323, 700));
		net.addNode(new Node("SAT7", 10, 140.323, 700));
		net.addNode(new Node("SAT8", -10, -177.323, 700));
		net.addNode(new Node("SAT9", -24.3, -140.323, 700));
		net.addNode(new Node("SAT10", -1.13, -95.323, 700));
		net.addNode(new Node("SAT11", 20.2312, -75.323, 700));
		net.init();
		Iterator<Node> nodeIter = net.getNodes().iterator();
		while(nodeIter.hasNext()) {
			Node node = nodeIter.next();
			System.out.println(node.neighboursToString());
		}
		PathFinder finder = new PathFinder(node_a, node_c);
		finder.findPath();
		assertEquals(9, finder.getBestPathLength());
	}
}
