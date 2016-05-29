/**
 * (c) Copyright 2016 Matti Kankkonen
 *
 * for Reaktor Orbital Challenge
 */
package com.kankkonen.matti.orbitalchal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class Node implements a network node in this satellite network model
 * The node can be either a satellite or a ground station, when 
 * the altitude is 0.
 * Each node has the information of its "neighbours" i.e. other
 * nodes in the network that are visible.
 * 
 * Calculations are based on right angle (90 degree) triangle trigonometry. 
 * 
 *   q
 *   |\
 *   | \
 * b |  \ c
 *   |   \
 *   L____\
 *     a
 *
 * For example if you know the length of two sides like b and c 
 * you can calculate angle q: 
 * 
 *    q = acos b/c 
 *    
 * So now if we imagine angle q here is at the centre of the earth 
 * and _c_ is a line connecting to a satellite orbiting the earth. 
 * Then _c_ would be earth's radius + satellite's altitude.
 * Then triangle's side _a_ would be line of sight from satellite 
 * to earth's horizon in practice a tangent with earth's surface = 
 * 90 degree angle. The consequence from this is that since _b_ is the
 * radius of earth, another side of triangle we know, we can then actually 
 * calculate angle q with acos.
 * 
 * The angle q tells us what's the "coverage" of the satellite to one direction, 
 * since it's degrees from the satellite's position to the location where 
 * the line of sight meets horizon.
 * 
 * To know if two nodes can "see" each other, seeing here means being above
 * the horizon, simply means that the coverage of these two nodes needs to 
 * overlap.
 * 
 * For that we need to calculate the "distance" (in degrees) of two node's 
 * coordinates. In order to do that we need to convert both node's lat,long 
 * coordinates into 3D vector with x,y,z. 
 * The angle between two 3D vectors can be calculated with formula:
 *   
 *   angle = arcos( ((x1*x2) + (y1*y2) + (z1*z2)) / (r1 * r2) )
 *   
 * Where r is the length of the vector, in our case that's the radius of earth.
 *   
 * With all this information we can simply check that "coverage" of two 
 * satellites 1 and 2 exceeds the "distance" between the satellites
 *   
 *   q1 + q2 >= angle
 * 
 */
public class Node {
	
	public final static double earth_radius = 6371;
	
	private String id;
	private double latitude;
	private double longitude;
	private double altitude;
	/* The angle alpha for trigonometric calculations
	 * this is in practise also our coverage 
	 * the tangent with earth surface */
	private double alpha;
	/* If I can see another node it's my neighbour */
	private Map<String,Node> neighbours;
	
	/**
	 * The Node representing a node in the network. It can be
	 * either a satellite or a ground station
	 * @param id unique identification for the node
	 * @param latitude of this node
	 * @param longitude of this node 
	 * @param altitude of this node or zero if ground station
	 */
	public Node(String id, double latitude, double longitude, double altitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		/*
		 * cos alpha = b/c
		 */
		this.alpha = Math.toDegrees(Math.acos(earth_radius / 
				(earth_radius + altitude)));
	
		this.neighbours = new HashMap<String,Node>();
	}

	public String getId() {
		return this.id;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	/**
	 * The coverage here means the angle value at the centre of the earth
	 * Between the axis at the node's coordinates e.g. pointing upwards to the satellite
	 * and the other axis at the edge of the satellites coverage i.e. where 
	 * the line of sight meets earth's surface the horizon 
	 * @return The angle in degrees
	 */
	public double getCoverage() {
		return this.alpha;
	}
	
	/**
	 * The "distance" between two node's coordinates in degrees
	 * @param b the Node instance we're comparing with
	 * @return angle in degrees
	 */
	public double getDistance(Node b) {
	/* Now we need to form 3D vectors from our and Node b's
	 coodrdinates (lat and long) into x,y,z so that we can 
	 calculate the angle between those two vectors. That angle
	 is what we consider here as the "distance" */
	
		double x1 = earth_radius * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(longitude));
		double y1 = earth_radius * Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(longitude));
		double z1 = earth_radius * Math.sin(Math.toRadians(latitude));
		
		double x2 = earth_radius * Math.cos(Math.toRadians(b.getLatitude())) * Math.cos(Math.toRadians(b.getLongitude()));
		double y2 = earth_radius * Math.cos(Math.toRadians(b.getLatitude())) * Math.sin(Math.toRadians(b.getLongitude()));
		double z2 = earth_radius * Math.sin(Math.toRadians(b.getLatitude()));
		
		return Math.toDegrees(Math.acos(((x1*x2) + (y1*y2) + (z1*z2))/(earth_radius * earth_radius)));
	}
	
	/**
	 * Can this node "see" the other Node.
	 * If the coverage of compared nodes overlaps it means
	 * that nodes are over each other's horizons and thus
	 * visible
	 * @param b the Node to compare with
	 * @return true if the other node is visible to this instance
	 */
	public boolean canSee(Node b) {
		double distance = this.getDistance(b);
		if((this.getCoverage() + b.getCoverage()) >= distance )
				return true;
		else
				return false;
	}
	
	public void addNeighbor(Node node) {
		this.neighbours.put(node.getId(), node);
		System.out.println(this.getId() + " added neighbour " + node.getId() +
				" distance:" + this.getDistance(node));
	}
	
	public String neighboursToString() {
		String neighbours = this.id + ": " +
		this.neighbours.size()+" neighbours";
		
		Iterator<Node>iter = this.neighbours.values().iterator();
		while(iter.hasNext()) {
			Node node = iter.next();
			neighbours = neighbours.concat(":" + node.id);
		}
		return neighbours;
	}
	
	/**
	 * Get a list of this Node's neighbours i.e.
	 * the nodes that are visible from this node instance
	 * @return Map object with visible Node instances
	 */
	public Map<String, Node> getNeighbours() {
		return this.neighbours;
	}
}
