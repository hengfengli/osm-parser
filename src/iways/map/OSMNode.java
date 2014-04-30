package iways.map;

/*************************************************************************
 *  File:  OSMNode.java
 *
 *  Contains all the need infomation about the <node> tag in the OSM file. 
 *
 *************************************************************************/
import java.util.*;

/**
 *  This class provide all the methods to access the properties of a node. 
 */
public class OSMNode{
	private String nodeID;
	private LonLat lonlat;
	private int index;
	
	public OSMNode(String nodeID, LonLat lonlat) {
		this.nodeID = nodeID;
		this.lonlat = lonlat;
		this.index  = -1;
	}
	
	// Access attributes
	public String getNodeID()          { return this.nodeID; }
	public LonLat getLonLat()          { return this.lonlat; }
	public int getIndex()              { return this.index;  }
	public void setIndex(int index)    { this.index = index; }
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