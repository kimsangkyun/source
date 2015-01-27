package sdd.interiorexplorers.map;

import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;

public class MapParser extends DefaultHandler {

	// private members to maintain context
	
	private Building tempBld = null;
	private Floor tempFloor = null;

	/**
	 * Constructor
	 */
	public MapParser() {
	}
	
	/**
	 * gets the current building
	 * @return current building
	 */
	public Building getTempBld() {
		return tempBld;
	}

	/**
	 * Parses an XML document
	 * @param in - input source for requested document
	 * @return - building representation of XML document
	 */
	public Building parseDocument(InputSource in) {

		return tempBld;

	}

	/**
	 * Handles a start element
	 * @param uri - uri of the element
	 * @param localName - local name of the element
	 * @param qName - qualified name of the element
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equalsIgnoreCase("Building")) {
			// create a new building
			tempBld = new Building();
			tempBld.setDescription(attributes.getValue("Description"));
			tempBld.setLattitude(BigDecimal.valueOf(Double
					.parseDouble(attributes.getValue("Lattitude"))));
			tempBld.setLongitude((BigDecimal.valueOf(Double
					.parseDouble(attributes.getValue("Longitude")))));
		} else if (localName.equalsIgnoreCase("Floor")) {
			// create a new floor
			tempFloor = new Floor();
			tempFloor.setId(Integer.parseInt(attributes.getValue("Id")));
		} else if (localName.equalsIgnoreCase("Node")) {
			// add a node to the current floor
			
			Floor.Node tempNode = new Floor.Node();
			tempNode.setId(Integer.parseInt(attributes.getValue("Id")));
			tempNode.setDescription(attributes.getValue("Description"));
			tempNode.setType(Types.fromValue(attributes.getValue("Type")));
			tempNode.setX(Integer.parseInt(attributes.getValue("X")));
			tempNode.setY(Integer.parseInt(attributes.getValue("Y")));
			tempFloor.getNode().add(tempNode);
			Log.i("test","hahaha : " + tempFloor.getNode().size());
		} else if (localName.equalsIgnoreCase("Edge")) {
			// add an edge to the current floor
			Floor.Edge tempEdge = new Floor.Edge();
			tempEdge.setFromId(Integer.parseInt(attributes.getValue("FromId")));
			tempEdge.setToId(Integer.parseInt(attributes.getValue("ToId")));
			tempFloor.getEdge().add(tempEdge);
		} else if (localName.equalsIgnoreCase("Elevator") && tempFloor != null) {
			// add an elevator to the current floor
			Floor.Elevator tempEle = new Floor.Elevator();
			tempEle.setId(Integer.parseInt(attributes.getValue("Id")));
			tempEle.setElevatorId(Integer.parseInt(attributes
					.getValue("ElevatorId")));
			tempEle.setX(Integer.parseInt(attributes.getValue("X")));
			tempEle.setY(Integer.parseInt(attributes.getValue("Y")));
			tempFloor.getElevator().add(tempEle);
		} else if (localName.equalsIgnoreCase("Elevator") && tempFloor == null) {
			// add an elevator to the current building
			Elevator tempEle = new Elevator();
			tempEle.setId(Integer.parseInt(attributes.getValue("Id")));
			tempEle.setType(Types.fromValue(attributes.getValue("Type")));
			tempEle.setDescription(attributes.getValue("Description"));
			tempBld.getElevator().add(tempEle);
		} else if (localName.equalsIgnoreCase("Area")) {
			// add an elevator to the current floor
			Floor.Area tempArea = new Floor.Area();
			tempArea.setId(Integer.parseInt(attributes.getValue("Id")));
			tempArea.setLeft(Integer.parseInt(attributes.getValue("Left")));
			tempArea.setRight(Integer.parseInt(attributes.getValue("Right")));
			tempArea.setTop(Integer.parseInt(attributes.getValue("Top")));
			tempArea.setBottom(Integer.parseInt(attributes.getValue("Bottom")));
			tempFloor.getArea().add(tempArea);
		}
	}

	/**
	 * Creates a string from an array of characters
	 * @param ch - character array
	 * @param start - index where string begins
	 * @param length - length of the string
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		new String(ch, start, length);
	}

	/**
	 * Handles an end element
	 * @param uri - uri of the element
	 * @param localName - local name of the element
	 * @param qName - qualified name of the element
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase("Floor")) {
			tempBld.getFloor().add(tempFloor);
		}

	}

}
