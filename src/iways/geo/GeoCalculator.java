package iways.geo;

/*************************************************************************
 *  File:  GeoCalculator.java
 *
 *  Provide all methods of computing geography.  
 *  
 *  Some part of codes from Geodesy by Mike Gavaghan
 *  http://www.gavaghan.org/blog/free-source-code/geodesy-library-vincentys-formula/
 *  
 *************************************************************************/
import iways.map.*;

public class GeoCalculator {
	
    /**
      * Calculate the distance (meters) between two geometric points. 
      */
	public static double calculateDistance(LonLat n1, LonLat n2) {
		double theta = n1.getLon() - n2.getLon();
		double dist = (
			    Math.sin(Math.toRadians(n1.getLat())) 
			  * Math.sin(Math.toRadians(n2.getLat())))
			  + (Math.cos(Math.toRadians(n1.getLat()))
				   * Math.cos(Math.toRadians(n2.getLat())) 
				   * Math.cos(Math.toRadians(theta))
				);
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1609.344;
		return dist;
	}
	
    /**
      * Calculate the Euclidean distance between two geometric points. 
      */
	public static double euclideanDistance(LonLat n1, LonLat n2) {
		return Math.sqrt(
			Math.pow(n1.getLon() - n2.getLon(),2) 
		  + Math.pow(n1.getLat() - n2.getLat(),2)
		);
	}
	
    /**
      * Calculate the distance (not meters) from a point to a line. 
      */
	public static double distancePointToLine(
		LonLat C, LonLat A, LonLat B) {
		double Cx = C.getLon(), Cy = C.getLat();
		double Ax = A.getLon(), Ay = A.getLat();
		double Bx = B.getLon(), By = B.getLat();
		
		double L_2 = Math.pow(Bx - Ax, 2) + Math.pow(By - Ay, 2);
		double L = Math.sqrt(L_2);

		double distance = Math.abs((Ay - Cy)*(Bx - Ax) 
			                     - (Ax - Cx)*(By - Ay))/ L;

		return distance;
	}
	
    /**
      * Return the projection point on the line (even this point is not 
	  * on this line). 
      */
	public static LonLat pointProjectionOnLine(LonLat C,
		LonLat A, LonLat B) {
		double Cx = C.getLon(), Cy = C.getLat();
		double Ax = A.getLon(), Ay = A.getLat();
		double Bx = B.getLon(), By = B.getLat();
		
		double L_2 = Math.pow(Bx-Ax, 2) + Math.pow(By-Ay, 2);

		double r = ((Ay-Cy)*(Ay-By) - (Ax-Cx)*(Bx-Ax))/ L_2;

		return new LonLat(Ax + r*(Bx-Ax), Ay + r*(By-Ay));
	}

    /**
      * Return the projection point on the line (even this point is not 
	  * on this line). 
      */
	public static boolean isInLineBounds(
		LonLat A, LonLat B, LonLat C) {
		
		double min_lon = Math.min(A.getLon(), B.getLon());
		double min_lat = Math.min(A.getLat(), B.getLat());
		double max_lon = Math.max(A.getLon(), B.getLon());
		double max_lat = Math.max(A.getLat(), B.getLat());
		
		Boundary boundary = new Boundary(
			new LonLat(min_lon, max_lat),
			new LonLat(max_lon, min_lat));

		return boundary.isInside(C);
	}
	
    /**
      * Calculate the angle of a vector, which starts from 'a' to 'b'.
      */
	public static int calcAngle(LonLat a, LonLat b) {
	    int angle = (int)Math.toDegrees(
			Math.atan2(b.getLat() - a.getLat(), b.getLon() - a.getLon()));
	    
		if(angle < 0) angle += 360;
	    return angle;
	}
	
    /**
      * Compute the difference of two degrees (0-360).
      */	
	public static int diffAngle(int a, int b) {
		int c = a - b;
		c = (c + 180) % 360 - 180;
		return Math.abs(c);
	}
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