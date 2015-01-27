package sdd.interiorexplorers.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sdd.interiorexplorers.graph.Edge;
import sdd.interiorexplorers.map.Building;
import sdd.interiorexplorers.map.Floor;

public class Graph {
	private List<Edge> edges;

	/**
	 * Constructor
	 */
	Graph() {
		edges = new ArrayList<Edge>();
	}

	/**
	 * Constructor - converts a building map into a graph
	 * @param currentMap - current building
	 */
	public Graph(Building currentMap) {
		edges = new ArrayList<Edge>();
		//search through each floor
		for (int i = 0; i < currentMap.getFloor().size(); ++i) {
			List<Floor.Edge> f = currentMap.getFloor().get(i).getEdge();
			List<Floor.Node> n = currentMap.getFloor().get(i).getNode();
			//search all the edges on the floor
			for (int j = 0; j < f.size(); ++j) {
				//get the two nodes on the current edge
				String n1 = String.valueOf(f.get(j).getFromId());
				String n2 = String.valueOf(f.get(j).getToId());
				//coordinate variables
				double x1 = 0;
				double y1 = 0;
				double x2 = 0;
				double y2 = 0;
				// get x and y coordinates of each node
				for (int k = 0; k < n.size(); ++k) {
					if (String.valueOf(n.get(k).getId()).equals(n1)) {
						x1 = n.get(k).getX();
						y1 = n.get(k).getY();
					}
					if (String.valueOf(n.get(k).getId()).equals(n2)) {
						x2 = n.get(k).getX();
						y2 = n.get(k).getY();
					}
				}
				//get the distance between the two nodes
				double dist = Math.sqrt(Math.pow(x1 - x2, 2.0)
						+ Math.pow(y1 - y2, 2.0));
				//add the edge to the graph
				this.addEdge(n1, n2, dist);
				System.out.println(n1+","+n2);
			}
		}
		//this.addEdge("8", "48", 0);
		//this.addEdge("9", "49", 0);
		//this.addEdge("10", "410", 0);
	}

	/**
	 * Adds an edge to the list of edges
	 * @param n1 - start node on edge
	 * @param n2 - end node on edge
	 * @param d - distance between the two nodes
	 */
	public void addEdge(String n1, String n2, double d) {
		edges.add(new Edge(n1, n2, d));
	}

	/**
	 * Removes an edge from the list of edges
	 * @param n1 - start node of edge to remove
	 * @param n2 - end node of edge to remove
	 */
	public void removeEdge(String n1, String n2) {
		//find all edges with both nodes n1 and n2, and remove them
		for (int i = 0; i < edges.size(); ++i) {
			if (edges.get(i).getTo().equals(n1)
					&& edges.get(i).getFrom().equals(n2)) {
				edges.remove(i);
				return;
			}
			if (edges.get(i).getTo().equals(n2)
					&& edges.get(i).getFrom().equals(n1)) {
				edges.remove(i);
				return;
			}
		}
	}

	/**
	 * Removes all edges with a certain node
	 * @param n1 - node to remove from the graph
	 */
	public void removeNode(String n1) {
		//find all edges that contain node n1 and remove them
		for (int i = 0; i < edges.size(); ++i) {
			if (edges.get(i).getTo().equals(n1)
					|| edges.get(i).getFrom().equals(n1)) {
				edges.remove(i);
				i--;
			}
		}
	}

	/**
	 * Prints all edges in the graph to the standard system output stream
	 */
	public void print() {
		//print edges
		for (int i = 0; i < edges.size(); ++i) {
			System.out.println(edges.get(i).getFrom() + edges.get(i).getTo());
		}
	}

	/**
	 * Finds all neighbors of a given Node
	 * @param n1 - node whose neighbors are desired
	 * @return all neighbors of n1 in the graph
	 */
	private List<String> getNeighbors(String n1) {
		List<String> neighbors = new ArrayList<String>();
		//search through all the edges
		for (int i = 0; i < edges.size(); ++i) {
			//if this edge contains n1, add the other edge node to the list
			if (edges.get(i).getTo().equals(n1)
					|| edges.get(i).getFrom().equals(n1)) {
				if (!neighbors.contains(edges.get(i).getTo())) {
					neighbors.add(edges.get(i).getTo());
				}
				if (!neighbors.contains(edges.get(i).getFrom())) {
					neighbors.add(edges.get(i).getFrom());
				}
			}
		}
		//remove n1 from the list of neighbors
		neighbors.remove(n1);
		return neighbors;
	}

	/**
	 * Finds the shortest path between two nodes
	 * @param start - start node
	 * @param end - end node
	 * @return - ordered list of nodes that make up the shortest path
	 */
	public List<String> Dijkstra(String start, String end) {
		HashMap<String, Double> distance = new HashMap<String, Double>();
		HashMap<String, String> previous = new HashMap<String, String>();
		// get all verticies
		List<String> Q = new ArrayList<String>();
		for (Edge e : edges) {
			if (!Q.contains(e.getFrom())) {
				Q.add(e.getFrom());
			}
			if (!Q.contains(e.getTo())) {
				Q.add(e.getTo());
			}
		}

		// initialize maps
		for (String v : Q) { 
			// Unknown distance function from source to v
			distance.put(v, Double.MAX_VALUE);
			// Previous node in optimal path from source
			previous.put(v, ""); 
		}
		// Distance from source to source
		distance.put(start, Double.valueOf(0.0)); 

		// All nodes in the graph are unoptimized - thus are in Q
		while (Q.size() != 0) {
			String u = Q.get(0);
			Double tempDist = distance.get(u);
			// find vertex in Q with smallest distance
			for (String s : Q) {
				if (distance.get(s) < tempDist) {
					u = s;
				}
			}
			// if target cannot be reached
			if (!distance.containsKey(u)) {
				break;
			}
			Q.remove(u);
			// update values of all neighbor verticies still in Q
			List<String> neighbors = this.getNeighbors(u);
			for (int i = 0; i < neighbors.size(); ++i) {
				if (!Q.contains(neighbors.get(i))) {
					neighbors.remove(i);
					--i;
				}
			}
			for (String v : neighbors) {
				// get the distance between u and v
				Double distanceBetween = Double.MAX_VALUE;
				for (Edge e : edges) {
					if ((e.getFrom().equals(u) && e.getTo().equals(v))
							|| (e.getFrom().equals(v) && e.getTo().equals(u))) {
						distanceBetween = Double.valueOf(e.getDistance());
						break;
					}
				}
				Double alt = distance.get(u) + distanceBetween;
				if (alt < distance.get(v)) {
					distance.put(v, alt);
					previous.put(v, u);
				}
			}
		}
		// build the path
		List<String> path = new ArrayList<String>();
		String u = end;
		while (previous.containsKey(u) && !previous.get(u).equals("")) {
			path.add(0, u);
			u = previous.get(u);
		}
		path.add(0, start);
		return path;
	}
}
