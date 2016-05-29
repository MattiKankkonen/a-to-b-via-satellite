/**
 * (c) Copyright 2016 Matti Kankkonen
 *
 * for Reaktor Orbital Challenge
 */

package com.kankkonen.matti.orbitalchal;

import java.io.File;
import java.util.Scanner;

public class Main {

	void createNetFromFile(String fileName) {
		
		try {
			Network net = new Network();
			PathFinder finder = null;
			Node start = null;
			Node end = null;
			
			Scanner scanner = new Scanner(new File(fileName));
			scanner.skip("#.*\\d*\\s*");
			scanner.useDelimiter("\\s|,");
			while(scanner.hasNext()) {
				String key = scanner.next();
				
				if(key.startsWith("SAT")) {
					System.out.println("Data line " + key );
					double latitude = scanner.nextDouble();
					double longitude = scanner.nextDouble();
					double altitude = scanner.nextDouble();
					System.out.println("lat " + latitude + " long " + longitude + " alt " + altitude);
					net.addNode(new Node(key, latitude, longitude, altitude));
				}
				else if(key.startsWith("ROUTE")) {
					System.out.println("Ground station line " + key);
					double lat_a = scanner.nextDouble();
					double long_a = scanner.nextDouble();
					double lat_b = scanner.nextDouble();
					double long_b = scanner.nextDouble();
					
					start = new Node("A", lat_a, long_a, 0);
					end = new Node("B", lat_b, long_b, 0);
					net.addNode(start);
					net.addNode(end);
					System.out.println(lat_a +","+ long_a +";"+ lat_b +","+ long_b);
				}
			}
			scanner.close();
			// All nodes added to network so init and create the PathFinder
			net.init();
			finder = new PathFinder(start, end);
			if( finder.findPath() == true )
				System.out.println("The shortest path is:" + finder.getBestPathString());
			else
				System.out.println("Couldn't find the path (;_;)");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new Main().createNetFromFile("c:\\Users\\Matti\\workspace\\OrbitChallenge\\src\\com\\kankkonen\\matti\\orbitalchal\\satellite_data.csv");
	}

}
