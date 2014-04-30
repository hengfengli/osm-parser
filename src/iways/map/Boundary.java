package iways.map;

/*************************************************************************
 *  File:  Boundary.java
 *
 *  Define a rectangle which contains two points in geography. 
 *
 *************************************************************************/

/**
 *  This class provide all the methods to access a rectangle in geography. 
 */
public class Boundary {
	private LonLat leftTop;      // the node on the left and top corner
	private LonLat rightBottom;  // the node on the right and bottom corner
	
	public Boundary(LonLat leftTop, LonLat rightBottom) {
		this.leftTop = leftTop;
		this.rightBottom = rightBottom;
	}
	
	/**
	 *  Check whether a point is inside this rectangle. 
	 */
	public  boolean isInside(LonLat l) {
		return l.getLon() >= leftTop.getLon() 
			&& l.getLon() <= rightBottom.getLon()
		    && l.getLat() >= rightBottom.getLat() 
		    && l.getLat() <= leftTop.getLat();
	}
	
	// Access attributes
	public LonLat getLeftTopNode() 		{ return this.leftTop; }
	public LonLat getRightBottomNode() 	{ return this.rightBottom; }
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