/**
 * (c) Copyright 2016 Matti Kankkonen
 *
 * for Reaktor Orbital Challenge
 */
package com.kankkonen.matti.orbitalchal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Network class holds the Node instances and initialises
 * the visibility information for each Node.
 */
public class Network {
	private List <Node>nodes = new ArrayList<Node>();
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	/**
	 * This needs to be called after adding all nodes
	 * Goes through the nodes and checks the visibility
	 * between each node and updates each node data correspondingly
	 */
	public void init() {
		Iterator<Node> iterator1 = nodes.iterator();
		
		while(iterator1.hasNext()) {
			Node current = iterator1.next();
			System.out.println("Node: " + current.getId());
			Iterator<Node> iterator2 = nodes.iterator();
			while(iterator2.hasNext()) {
				Node candidate = iterator2.next();
				if(candidate != current && current.canSee(candidate) == true ) {
					current.addNeighbor(candidate);
				}
			}
		}
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
}
