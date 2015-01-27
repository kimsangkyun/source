package sdd.interiorexplorers.map;

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.10.18 at 12:04:40 AM EDT 
//

/**
 * <p>
 * Java class for Elevator complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Elevator&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;attribute name=&quot;Id&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;Type&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;Description&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */

public class Elevator {

	// protected members
	
	protected int id;

	protected Types type;

	protected String description;

	/**
	 * Gets the value of the id property.
	 * 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 */
	public void setId(int value) {
		this.id = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 */
	public Types getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 */
	public void setType(Types value) {
		this.type = value;
	}

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

}