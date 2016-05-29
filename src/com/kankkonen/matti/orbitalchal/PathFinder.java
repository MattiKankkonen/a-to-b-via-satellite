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
 * PathFinder calculates paths between given node A
 * and node B. The strategy is to use Path class instance for
 * each route option. Starting with one isntance from the node A and
 * checking how many "neighbours" it has. Each "neighbour"
 * is in practice a path option, causing branching and creation
 * of a new Path instance. All Paths are added to a list of
 * processed paths and "stepped" one by one.
 * 
 * To prevent looping and unnecessary 
 * overlapping each Path instance has a list of "no go" nodes
 * which are the nodes already visited or to be handled by other
 * branches.
 * 
 * Each branching requires cloning of the already travelled route
 * and the list of "no go" nodes.
 * 
 * After the destination is found or a dead end the path is removed
 * from the list of processed paths.
 * When the list is empty, we check results and which path is 
 * the shortest if any valid was found.
 *
 * The "processed list" is owned by the PathFinder but the Path instances
 * do the adding and deleting to the list
 */
public class PathFinder {

	private Node start;
	private Node end;
	private List<Path> paths;
	private List<Path> hits;
	private boolean validPathFound = false;
	private Path bestPath = null;
	private int pathId = 0;
	
	/**
	 * 
	 * @param start Node of the path
	 * @param end Node of the path
	 */
	public PathFinder(Node start, Node end) {
		this.start = start;
		this.end = end;
		this.paths = new ArrayList<Path>();
		hits = new ArrayList<Path>();
	}
	
	/**
	 * The constructor told from where to where, now do it
	 * and search the optimal path
	 */
	public boolean findPath() {
		
		// First add the starting point to the list
		paths.add(new Path(this, null, start, end));
		Path path = null;
		
		/*
		 * This is the "engine" i.e. we create the first Path item
		 * and then keep on stepping the paths until all the options
		 * have been checked. The first Path item will be branching
		 * the path by creating new Path instances for each branch
		 * of the path
		 */
		while(paths.size() > 0) {
			// each iteration uses a copy of the original path list
			// so that original can be modified from Path objects
			List<Path> pathsClone = new ArrayList<Path>();
			pathsClone.addAll(paths);
			Iterator<Path> i = pathsClone.iterator();
			
			while(i.hasNext()) {
				path = i.next();
				if(path.go() == true) {
					// Collect all paths leading to the destination
					// for further evaluation
					hits.add(path);
					validPathFound = true;
				}
			}		
		}
		
		/* 
		 * The "engine" has stopped and all the path options have
		 * been exhausted. Let's check if we found the shortest path
		 */
		Iterator<Path> iter = hits.iterator();
		int shortest = Integer.MAX_VALUE;
		double shortestDistance = Double.MAX_VALUE;
		
		while(iter.hasNext()) {
			path = iter.next();
			System.out.println("Path length:" + path.steps);
			path.listPath();
			if(path.getSteps() <= shortest && path.getPathLength() < shortestDistance) {
				shortest = path.getSteps();
				bestPath = path;
				shortestDistance = path.getPathLength();
			}
		}
		
		if(bestPath != null) {
			System.out.println("\nThe shortest path from:" + start.getId() +
					" to:" + end.getId() + " is ");
			bestPath.listPath();
		}		
		return validPathFound;
	}
	
	/**
	 * 
	 * @return the length of the optimal path or 0
	 */
	public int getBestPathLength() {
		if(bestPath == null)
			return 0;
		else
			return bestPath.steps;
	}
	
	/**
	 * 
	 * @return String of comma separated list of satellites in the optimal path
	 */
	public String getBestPathString() {
		String pathString = "null";
		if(bestPath != null) {
			pathString = "";
			Iterator<Node> nodes = bestPath.getPath().iterator();
			while( nodes.hasNext()) {
				 Node node = nodes.next();
				 if(node != start && node != end){
					 if(pathString != "")
						 pathString = pathString.concat(",");
					 
					 pathString = pathString.concat(node.getId());
				 }
			}
		}
		return pathString;
	}
	
	/**
	 * Has the PathFinder found a valid path?
	 * @return boolean
	 */
	public boolean foundValidPath() {
		return validPathFound;
	}
	
	private int createPathId() {
		return ++pathId;
	}
	
	/**
	 * Path object does the actual work for the PathFinder
	 * Ever since from the starting point, check the "neighbours"
	 * of the Node i.e. visible Node's and create a branch/new Path
	 * for those. Repeat and rinse on every step on the paths.
	 *
	 * NOTE that Path objects are the ones manipulating the path list
	 * owned by PathFinder.
	 */
	private class Path {
		
		private int id = 0;
		private int steps = -1;
		private boolean validPath = false;
		private List<Node> track = null;
		private List<Node> doNotGo = null;
		private Node next = null;
		private Node current = null;
		private Node end = null;
		private PathFinder finder = null;
		
		// The sum of "distances" between the nodes on the path
		private double pathLength = 0;
		
		Path(PathFinder finder, Path previous, Node next, Node end) {
			this.finder = finder;
			this.end = end;
			this.next = next;
			track = new ArrayList<Node>();
			doNotGo = new ArrayList<Node>();
			
			if(previous != null) {
				// "we're already on the path", clone data from this path's 
				// previous steps
				steps = previous.getSteps();
				track.addAll(previous.getPath());
				doNotGo.addAll(previous.getDoNotGo());
				pathLength = previous.getPathLength();
				id = finder.createPathId();
			}
		}
		
		private int getId() {
			return id;
		}

		private double getPathLength() {
			return pathLength;
		}
		

		/**
		 * This is where the "thinking happens". Take a step on the path
		 * and then analyse the results.
		 * @return true if the end point was reached
		 */
		private boolean go() {
			// take the step
			steps++;
			current = next;
			track.add(current);			
			next = null;
			doNotGo.add(current); // Don't walk back the path and loop
			
			// are we done with this?
			// a valid path found
			if(current == end) {
				System.out.println("-- Found a valid path:" + this.getId());
				validPath = true;
				finder.paths.remove(this);
				return true;
			}
			// this path doesn't lead where we want
			if(current == null) {
				System.out.println("-- Dead end here - path:" + this.getId());
				validPath = false;
				finder.paths.remove(this);
				return false;
			}
			
			String separator = " -";
			for(int i=0; i< this.getId(); i++)
				separator = separator.concat("-");
			System.out.println("Path " + this.getId() + 
					separator + " " + current.getId());
			
			// ok, so where to go next?
			Iterator<Node> i = current.getNeighbours().values().iterator();
			while(i.hasNext()) {
				Node neighbour = i.next();
				// is this where we wanna go next?
				if(doNotGo.contains(neighbour) == false) {
					if(next == null) { 
						// ok, let's go!
						next = neighbour;
						pathLength += current.getDistance(next);
					} else {
						// we already know where we go but more
						// interesting places exist, so branch here
						// a new Path instance
						doNotGo.add(neighbour);						
						finder.paths.add(new Path(finder, this,
								neighbour, this.end));
					}
				}
			}
			return false;
		}
		
		private List<Node> getPath() {
			return track;
		}
		
		private List<Node> getDoNotGo() {
			return doNotGo;
		}
		
		private int getSteps() {
			return steps;
		}
		private void listPath() {
			Iterator<Node> i = track.iterator();
			int hop = 0;
			while(i.hasNext()) {
				Node n = i.next();				
				if(n != null) {
					System.out.println("Hop:" + hop + " " + 
							n.getId() + "     lat:" + n.getLatitude() +
							" long:" + n.getLongitude() + " alt:" + n.getAltitude());
				} else {
					System.out.println("Hop:" + hop + " null");
				}
				hop++;
			}
			System.out.println("Path: " + getId() + " distance:" + getPathLength() + "\n");
		}
		
	}
}
