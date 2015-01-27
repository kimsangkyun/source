package sdd.interiorexplorers.graph;

public class Edge {

	// private members
	
	private String from;
	private String to;
	private double distance;

	/**
	 * Constructor
	 * @param from - node 1 in the edge
	 * @param to - node 2 in the edge
	 * @param distance - distance between the two nodes
	 */
	Edge(String from, String to, double distance) {
		this.from = from;
		this.to = to;
		this.distance = distance;
	}

	/**
	 * Gets the first node of the edge
	 * @return - Name of first node
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets the first node of the edge
	 * @param from - Name of first node
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets the second node of the edge
	 * @return - Name of second node
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets the second node of the edge
	 * @param to - Name of second node
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Gets the distance between the two edge nodes
	 * @return - distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Sets the distance between the two edge nodes
	 * @param distance - distance between the two edge nodes
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
