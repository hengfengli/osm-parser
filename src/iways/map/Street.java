package iways.map;

/*************************************************************************
 *  File:  Street.java
 *
 *  Contains all the infomation about a street, which is similar with 
 *  the concept of a edge in Graph. 
 *
 *************************************************************************/

import iways.geo.GeoCalculator;

/**
 *  This class provide all the methods to access the properties of a street.
 */
public class Street {
	private int streetID;
	private OSMNode startNode;
	private OSMNode endNode;
	private double distance;
	private int level;         // types of street, like motorway, residential
	private int angle;         // angle of street
	
	public Street(int streetID, OSMNode startNode, OSMNode endNode, 
		double distance, int level) {
		this.startNode  = startNode;
		this.endNode    = endNode;
		this.distance   = distance;
		this.streetID   = streetID;
		this.level      = level;
		this.angle      = (int)GeoCalculator.calcAngle(startNode.getLonLat(), 
			                            endNode.getLonLat());
	}
	
	// Access attributes
	public double getDistance()    { return this.distance; }
	public OSMNode getStartNode()  { return this.startNode;}
	public OSMNode getEndNode()    { return this.endNode;  }
	public int getStreetID()       { return this.streetID; }
    public String getStartNodeID() { return this.startNode.getNodeID(); }
    public String getEndNodeID()   { return this.endNode.getNodeID();   }
	public int getLevel()          { return this.level; }
	public int getAngle()          { return this.angle; }
}


/*************************************************************************
 *  Copyright 2013, Hengfeng Li.
 *
 *  ExtractGraph.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExtractGraph.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with ExtractGraph.jar.  If not, see http://www.gnu.org/licenses.
 *************************************************************************/