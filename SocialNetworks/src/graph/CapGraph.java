/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	private HashMap<Integer, HashSet<Integer>> graph = new HashMap<>();
	private int nVertex = 0;
	private int nEdge = 0;
	
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		if (!graph.containsKey(num)) {
			graph.put(num, new HashSet<Integer>());
			nVertex++;
		}
		
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		if (!graph.containsKey(from)) {
			this.addVertex(from);
		}
		if (!graph.containsKey(to)) {
			this.addVertex(to);
		}
		if (to != from) {
			graph.get(from).add(to);
			nEdge++;
		}
	}

	public int numVertex() {
		return nVertex;
	}
	
	public int numEdge() {
		return nEdge;
	}
	
	public Set<Integer> getVertices() {
		return graph.keySet();
	}
	
	public boolean hasVertex(int vertex) {
		if (graph.containsKey(vertex)) {
			return true;
		}
		return false;
	}
	
    public boolean hasEdge(int from, int to) {
    	if (graph.containsKey(from)) {
    		return graph.get(from).contains(to);
    	}
    	return false;
    }
	
	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		Graph ego = new CapGraph();
		
		// add center
		if (!graph.containsKey(center)) {
			return ego; // early termination returning empty graph
		}
		ego.addVertex(center);
		
		// add all vertices that are connected to center
		for (int to : graph.get(center)) {
			ego.addVertex(to);
			ego.addEdge(center, to);
		}
		
		Set<Integer> vertices = ego.getVertices();
		
		// add edges among the vertices in ego
		for (int from : vertices) {
			for (int to : vertices) {
				if (this.hasEdge(from, to)) {
					ego.addEdge(from, to);
				}
			}
		}
		
		return ego;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		
		return new ArrayList<Graph>();
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return graph;
	}

}
