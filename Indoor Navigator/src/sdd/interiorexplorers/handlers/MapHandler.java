package sdd.interiorexplorers.handlers;

import java.util.ArrayList;
import java.util.List;

import sdd.interiorexplorers.graph.Graph;
import sdd.interiorexplorers.map.*;

public class MapHandler {

	// private members
	private static MapHandler mapHandler = new MapHandler();
	private List<Building> listMaps;
	private Building currentMap = null;
	private Graph currentGraph;

	/**
	 * Private constructor
	 */
	private MapHandler() {
		listMaps = new ArrayList<Building>();
	}

	/**
	 * Gets the private MapHandler instance
	 * 
	 * @return the current MapHandler instance
	 */
	public static MapHandler getInstance() {
		return mapHandler;
	}

	/**
	 * Gets the current graph
	 * 
	 * @return a graph
	 */
	public Graph getCurrentGraph() {
		return currentGraph;
	}

	/**
	 * Gets the list of maps
	 * 
	 * @return list of loaded maps
	 */
	public List<Building> getListMaps() {
		return listMaps;
	}

	/**
	 * Adds a building to the list of maps
	 * 
	 * @param a
	 *            building map
	 */
	public void addMapToList(Building building) {
		// make sure duplicates are not added
		if (building != null && !this.listMaps.contains(building)) {
			this.listMaps.add(building);
		}
	}

	/**
	 * Gets the current map
	 * 
	 * @return currently loaded map
	 */
	public Building getCurrentMap() {
		return currentMap;
	}

	/**
	 * Sets the current map
	 * 
	 * @param desired
	 *            current map
	 */
	public void setCurrentMap(Building currentMap) {
		addMapToList(currentMap);
		this.currentMap = currentMap;
		this.currentGraph = new Graph(currentMap);
	}

}
