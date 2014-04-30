package iways.map;

/*************************************************************************
 *  File:  PreProcessingMap.java
 *
 *  Parsing OSM file into a graph.
 *  
 *************************************************************************/

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import iways.geo.*;

/**
 *  This class provide all the methods to read OSM file and generate edges 
 *  and nodes. 
 */
public class PreProcessingMap {
	
	/**
	 *  Load OSM file from given path and create a graph.
	 */
	public static void load(String path) {
		
		try {
            		String vertexFilename = "vertex.txt";
            		String edgeFilename   = "edges.txt";
            		String streetFilename = "streets.txt";
            		boolean isDirected = true;
            
			PreProcessingMap preProcessingMap = new PreProcessingMap();
			
			Document doc = preProcessingMap.createXMLDocument(path);
			// read the boundary of this graph
			Boundary boundary = preProcessingMap.readBoundaryFromOSMFile(doc);
			
			// Read the data of OSM nodes
			System.out.println("read OSM nodes...");
			List<OSMNode> osmNodes = preProcessingMap.readNodesFromOSMFile(doc);
			// Create OSM node hashtable
			System.out.println("Create index hashtable for OSM nodes ");
			Map<String, OSMNode> nodesMap = new HashMap<String, OSMNode>();
			preProcessingMap.createNodesHashtable(osmNodes, nodesMap);

			// Read the data of OSM ways
			System.out.println("read ways...");
			List<OSMWay> osmWays = preProcessingMap.readWaysFromOSMFile(doc);
			System.out.println("ways size:" + osmWays.size());
			
			// Create streets, 'true' means to generate a directed graph
			List<Street> streets = preProcessingMap.createStreets(
				nodesMap, osmWays, isDirected);
			
			// Clean those nodes that is not used in this graph and output 
			// them into a file.
			List<OSMNode> requiredNodes = preProcessingMap.selectNodes(streets);
			System.out.println("node size:" + requiredNodes.size());
			preProcessingMap.outputNodes(requiredNodes, vertexFilename);
			
			// Output streets into file
			preProcessingMap.outputStreets(streets, 
				streetFilename);
			preProcessingMap.outputEdges(requiredNodes.size(), streets, 
				edgeFilename);
			System.out.println("streets size:" + streets.size());
			
			
			// Output the adjacent list into file
			// preProcessingMap.outputAdjacentList(requiredNodes);
			
			System.out.println("Building Graph - Done");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  Select all the nodes from edges and return a list of nodes (each node
	 *  appears only once).
	 */
	public List<OSMNode> selectNodes(List<Street> streets) {
		Map<String, OSMNode> requiredNodes = new HashMap<String, OSMNode>();
		
		for (int i = 0; i < streets.size(); i++) {
			Street street = streets.get(i);
			
			OSMNode startnode = street.getStartNode();
			OSMNode endnode   = street.getEndNode();
			requiredNodes.put(startnode.getNodeID(), startnode);
			requiredNodes.put(endnode.getNodeID(), endnode);
		}
		
		return new ArrayList<OSMNode>(requiredNodes.values());
	}
	
	/**
	 *  Write information of streets into a file. (for some special purposes)
	 */
	public void outputStreets( 
		List<Street> streets, String path) throws IOException {
		
		File file = new File(path);
 
		// if file doesn't exist, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw     = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int numEdges = streets.size();
		bw.write(numEdges + "\n");
		
		for (int i = 0; i < streets.size(); i++) {
			Street street        = streets.get(i);
			OSMNode startOSMNode = street.getStartNode();
			OSMNode endOSMNode   = street.getEndNode();
			
			bw.write(street.getStreetID() + " "
				+ startOSMNode.getIndex() + " "
				+ startOSMNode.getLonLat().getLon() + " " 
				+ startOSMNode.getLonLat().getLat() + " " 
				+ endOSMNode.getIndex() + " "
				+ endOSMNode.getLonLat().getLon() + " " 
				+ endOSMNode.getLonLat().getLat() + " "
				+ street.getDistance() + " "
				+ street.getLevel() + " " 
				+ street.getAngle() + "\n");
		}
		
		bw.close();
	}

	/**
	 *  Write information of edges into a file. 
	 */
	public void outputEdges(int numVertices, List<Street> streets, String path) 
		throws IOException {
		
		File file = new File(path);
 
		// if file doesn't exist, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		int numEdges = streets.size();
		
		bw.write(numVertices + "\n");
		bw.write(numEdges + "\n");
		
		for (int i = 0; i < streets.size(); i++) {
			Street street = streets.get(i);
			OSMNode startOSMNode = street.getStartNode();
			OSMNode endOSMNode = street.getEndNode();
			
			bw.write(
				street.getStreetID() + " "
				+ startOSMNode.getIndex() + " " 
				+ endOSMNode.getIndex() + " "
				+ street.getDistance() + "\n");			
		}
		
		bw.close();
	}
	
	/**
	 *  Create streets from OSMNodes and OSMWays. 
	 */
	public List<Street> createStreets(Map<String, OSMNode> nodes, 
		List<OSMWay> osmWays, boolean isDirected) {

		List<Street> streets = new ArrayList<Street>();
		
		int count = 0;
		
		// generate the streets
		for (int i = 0; i < osmWays.size(); i++) {
			OSMWay thisStreet = osmWays.get(i);
			List<String> refs = thisStreet.getRefs();
			int level = thisStreet.getLevel();

			for (int j = 1; j < refs.size(); j++) {
				String start = refs.get(j - 1);
				OSMNode startNode = nodes.get(start);
				String end = refs.get(j);
				OSMNode endNode = nodes.get(end);
				
				if (startNode == null || endNode == null) { continue; }
				
				// Calculate the distance
				double distance = GeoCalculator.calculateDistance(
					startNode.getLonLat(), endNode.getLonLat());
				
				streets.add(new Street(count, startNode, endNode, 
					distance, level));
				count++;
				
				// check whether needs to generate a directed graph or not, and
				// whether this street is one-way.
				if (isDirected && !thisStreet.oneway()) {
					streets.add(new Street(count, endNode, startNode, 
						distance, level));
					count++;
				}
				
			}
		}

		return streets;
	}
	
	/**
	 *  Create a hash table of nodes for search purpose
	 */
	public void createNodesHashtable(List<OSMNode> osmNodes,
		Map<String, OSMNode> nodesMap) {
		
		for (int i = 0; i < osmNodes.size(); i++) {
			OSMNode node = osmNodes.get(i);
			
			nodesMap.put(node.getNodeID(), node);
		}
	}
	
	/**
	 *  Read a xml file and return a document object. 
	 */
	public Document createXMLDocument(String path) 
		throws FileNotFoundException, ParserConfigurationException, 
		SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		File file = new File(path);
		InputStream inputStream = new FileInputStream(file);
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		InputSource is = new InputSource(reader);

		Document doc = db.parse(is);

		doc.getDocumentElement().normalize();

		return doc;		
	}
	
	/**
	 *  Read the boundary of this document.
	 */
	public Boundary readBoundaryFromOSMFile(Document doc) {
		// For example, a 'bounds' tag
		// <bounds minlat="-37.7494600" minlon="144.9470500" maxlat="-37.7358200" maxlon="144.9599000"/>
		
		NodeList nodesList = doc.getElementsByTagName("bounds");
		
		double left, top, right, bottom; 
		
		// because there is only one <bounds> tag in a OSM file
		Node item = nodesList.item(0);
		NamedNodeMap attrsMap = item.getAttributes();
		
		LonLat leftTop = new LonLat(
			Double.parseDouble(attrsMap.getNamedItem("minlon").getNodeValue()),
			Double.parseDouble(attrsMap.getNamedItem("maxlat").getNodeValue())
		);

		LonLat rightBottom = new LonLat(
			Double.parseDouble(attrsMap.getNamedItem("maxlon").getNodeValue()),
			Double.parseDouble(attrsMap.getNamedItem("minlat").getNodeValue())
		);
				
		return new Boundary(leftTop, rightBottom);
	}
	
	/**
	 *  Write all the vertices into a file. 
	 */
	public void outputNodes(List<OSMNode> nodes, String path) 
		throws IOException {
		
		File file = new File(path);
 
		// if file doesn't exist, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
			
		int size = nodes.size();
			
		bw.write(size + "\n");
			
		for (int i = 0; i < size; i++) {
			OSMNode node = nodes.get(i);
			
			// set the index for a node
			node.setIndex(i);
			// output the node's ID, longitude, latitude
			bw.write(i + " " + node.getNodeID() + " " 
				+ node.getLonLat().getLon() + " " 
				+ node.getLonLat().getLat() + "\n");
		}
		
		bw.close();
	}
	
	/**
	 *  Read all OSM nodes from a OSM file. 
	 */
	public List<OSMNode> readNodesFromOSMFile(Document doc) {
		
		List<OSMNode> osmNodes = new ArrayList<OSMNode>();
				
		NodeList nodesList = doc.getElementsByTagName("node");
		int length = nodesList.getLength();
			
		System.out.println("Number of total OSM Nodes:" 
			+ nodesList.getLength());
		for (int i = 0; i < length; i++) {
			
			Node item = nodesList.item(i);
			NamedNodeMap attrsMap = item.getAttributes();
			
			LonLat lonlat = new LonLat(
				Double.parseDouble(attrsMap.getNamedItem("lon").getNodeValue()),
				Double.parseDouble(attrsMap.getNamedItem("lat").getNodeValue())
			);
			
			String nodeID = attrsMap.getNamedItem("id").getNodeValue();
			
			osmNodes.add(new OSMNode(nodeID, lonlat));
		}
		
		return osmNodes;
	}
	
	/**
	 *  Read all attributes from 'tag', (here, 'Node' is a XML node, which is 
	 *  not a vertex). 
	 */
	public Map<String, String> readTagMapFromOSMFile(Node item) {
		Map<String, String> tags = new HashMap<String, String>();
		
		// Get all tags
		if (item.getNodeType() == Node.ELEMENT_NODE) {
			Element itemElement = (Element)item;

			NodeList tagsList = itemElement.getElementsByTagName("tag");
			int length = tagsList.getLength();

			for (int j = 0; j < length; j++) {
				NamedNodeMap tagAttrsMap = tagsList.item(j).getAttributes();

				tags.put(tagAttrsMap.getNamedItem("k").getNodeValue(),
						tagAttrsMap.getNamedItem("v").getNodeValue());

			}
		}
		
		return tags;
	}

	/**
	 *  Read all attributes from 'nd', (here, 'Node' is a XML node, which is 
	 *  not a vertex). 
	 */
	public List<String> readRefsOfWayFromOSMFile(Node item) {
		List<String> refs = new ArrayList<String>();

		if (item.getNodeType() == Node.ELEMENT_NODE) {
			Element itemElement = (Element) item;

			NodeList refsList = itemElement.getElementsByTagName("nd");
			int length = refsList.getLength();

			for (int j = 0; j < length; j++) {
				NamedNodeMap nodeAttrsMap = refsList.item(j).getAttributes();
				
				// 'ref' is the identity of a OSM node
				refs.add(nodeAttrsMap.getNamedItem("ref").getNodeValue());
			}
		}

		return refs;
	}
	
	/**
	 *  Read 'way' tags and create OSM ways. 
	 */
	public List<OSMWay> readWaysFromOSMFile(Document doc) {
		List<OSMWay> osmWays = new ArrayList<OSMWay>();

		NodeList waysList = doc.getElementsByTagName("way");
		// NOTICE: don't put this into condition part of a for-loop,
		//         because getLength() is a O(n) function.
		int length = waysList.getLength();
		
		// load all ways from the osm file
		for (int i = 0; i < length; i++) {
			Node item = waysList.item(i);
			NamedNodeMap attrsMap = waysList.item(i).getAttributes();
			String wayID = attrsMap.getNamedItem("id").getNodeValue();

			// read the 'tags' and 'refs' of this way
			Map<String, String> tags = readTagMapFromOSMFile(item);
			List<String> refs = readRefsOfWayFromOSMFile(item);

			String highway = tags.get("highway");
			String railway = tags.get("railway");
			if (((highway != null) 
			  && !highway.equals("footway")
			  && !highway.equals("cycleway")
			  && !highway.equals("pedestrian")
			  // if want to only use primary streets, uncomment this 
			  // condition. 				  
			  // && !highway.equals("residential") 
			  && !highway.equals("service") 
			  && !highway.equals("unclassified") 
		      && !highway.equals("platform")
			  && !highway.equals("access_ramp")
  		      && !highway.equals("steps")
			  && !highway.equals("path"))) {
			  // || ((railway != null) && !railway.equals("tram"))) {
				osmWays.add(new OSMWay(wayID, refs, tags));
			}
		}

		return osmWays;
	}
	
	public static void main(String [] args) {
		if (args.length == 1) {
			PreProcessingMap.load(args[0]);
		} 
		else {
			System.out.println("Your should specify an OSM file as input!");
			System.out.println("Usage: program <input_file>");
		}
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
