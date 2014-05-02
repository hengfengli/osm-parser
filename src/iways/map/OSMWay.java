package iways.map;

/*************************************************************************
 *  File:  OSMWay.java
 *
 *  Contains all the need infomation about the <way> tag in the OSM file. 
 *
 *************************************************************************/

import java.io.*;
import java.util.*;

/**
 *  This class provide all the methods to access the properties of a way. 
 *  In OSM file, the 'way' means an entire street, which may contain
 *  many intersections. However, in a graph, an edge itself cannot have any
 *  nodes except a start node and an end node. 
 */
public class OSMWay {
	private String id;                    // its corresponding ID in OSM file
	private List<String> refs;            // all the nodes
	private Map<String, String> tags;     // all the attributes for this way
	private int level;                    // type of this way
	private boolean isOneway = false;     // oneway?

	public OSMWay(String id, List<String> refs, Map<String, String> tags, int level) {
		this.id = id;
		this.refs = refs;
		this.tags = tags;
		
        this.level = level;
        
        // // classify different types of roads
        // // the split function is to handle with the case: "tertiary_link"
        // String[] labels = tags.get("highway").split("_");
        // String highway = labels[0];
        // 
        // if      (highway.equals("motorway"))    { this.level = 1; }
        // else if (highway.equals("primary"))     { this.level = 2; }
        // else if (highway.equals("secondary"))   { this.level = 3; }
        // else if (highway.equals("tertiary"))    { this.level = 4; }
        // else if (highway.equals("trunk"))       { this.level = 5; }
        // else if (highway.equals("residential")) { this.level = 6; }
        // else                                    { this.level = 7; }
		
		// check whether this way is one-way or not
		String oneway = tags.get("oneway");
		if (oneway != null && oneway.equals("yes")) {
			isOneway = true;
		}
	}
	
	// Access attributes
	public String getID()                { return this.id;    }
	public List<String> getRefs()        { return this.refs;  }
	public Map<String, String> getTags() { return this.tags;  }
	public int getLevel()                { return this.level; }
	public boolean oneway()              { return this.isOneway; }
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