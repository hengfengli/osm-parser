package iways.map;

/*************************************************************************
 *  File:  LonLat.java
 *
 *  Provide methods about a point in geography. 
 *
 *************************************************************************/

/**
 *  This class provide all the methods to access a point in geography. 
 */
public class LonLat {
	private double lon;
	private double lat;
	
	public LonLat(double lon, double lat) {
		this.lon = lon;
		this.lat = lat;
	}
	
	// Selectors and Mutators
	public double getLon()           { return lon; }
	public void   setLon(double lon) { this.lon = lon; }
	public double getLat()           { return lat; }
	public void   setLat(double lat) { this.lat = lat; }
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