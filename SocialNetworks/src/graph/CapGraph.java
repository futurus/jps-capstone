/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	private HashMap<Integer, HashSet<Integer>> graph = new HashMap<>();
	private HashMap<Integer, HashSet<Integer>> graphTranspose = new HashMap<>();
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
			
			graphTranspose.put(num, new HashSet<Integer>());
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
			graphTranspose.get(to).add(from);
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
	
	public Set<Integer> getNeighbors(int v) {
		return graph.get(v);
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
		List<Graph> sccs = new ArrayList<Graph>();
		
		Set<Integer> visited = new HashSet<>();
		Stack<Integer> vertices = dfs(graph, this.getVertices());
		
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			
			if (!visited.contains(v)) {
				Graph g = new CapGraph();
				dfs_visit(graphTranspose, v, visited, null, g);
				sccs.add(g);
			}
		}
		
		return sccs;
	}

	public Stack<Integer> dfs(HashMap<Integer, HashSet<Integer>> g, Set<Integer> vs) {
		// quickly convert to Stack for operation
		Stack<Integer> vertices = new Stack<>();
		vertices.addAll(vs);
		
		Set<Integer> visited = new HashSet<>();
		Stack<Integer> finished = new Stack<>();
		
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			
			if (!visited.contains(v)) {
				dfs_visit(g, v, visited, finished, null);
			}
		}
		
		return finished;
	}
	
	public void dfs_visit(HashMap<Integer, HashSet<Integer>> g, int v, Set<Integer> visited, Stack<Integer> finished, Graph newg) {
		visited.add(v);
		
		// build SSC if newg is not null
		if (newg != null)
			newg.addVertex(v);
		
		for (int n : g.get(v)) {
			if (!visited.contains(n)) {
				dfs_visit(g, n, visited, finished, newg);
			}
		}
		if (finished != null)
			finished.add(v);
	}
	
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return graph;
	}

}
