package graph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import util.GraphLoader;
import graph.CapGraph;
import graph.Graph;

public class CapGraphTest {
	Graph graph;
	
	@Before
	public void setUp() throws Exception {
		graph = new CapGraph();
        //GraphLoader.loadGraph(graph, "data/facebook_ucsd.txt");
	}

	@Test
	public void addVertex() {
		graph.addVertex(0);
		assertEquals(1, graph.numVertex());
		assertEquals(0, graph.numEdge());
	}

	@Test
	public void addEdge0() {
		graph.addVertex(0);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		assertEquals(3, graph.numVertex());
		assertEquals(2, graph.numEdge());
	}
	
	@Test
	public void addEdge1() {
		graph.addVertex(0);
		graph.addEdge(0, 0);
		graph.addEdge(0, 1);
		assertEquals(2, graph.numVertex());
		assertEquals(1, graph.numEdge());
	}
	
	@Test
	public void hasVertex() {
		graph.addVertex(0);
		graph.addEdge(0, 1);
		assertTrue(graph.hasVertex(0));
		assertTrue(graph.hasVertex(1));
		assertFalse(graph.hasVertex(5));
	}
	
	@Test
	public void hasEdge() {
		graph.addVertex(0);
		graph.addEdge(0, 1);
		graph.addEdge(1, 5);
		assertTrue(graph.hasEdge(0, 1));
		assertTrue(graph.hasEdge(1, 5));
		assertFalse(graph.hasEdge(5, 1));
		assertFalse(graph.hasEdge(0, 5));
	}
	
	/*@Test
	public void egoNet() {
		GraphLoader.loadGraph(graph, "data/facebook_ucsd.txt");
		Graph ego = graph.getEgonet(22);
		System.out.println(ego.exportGraph());
		assertTrue(true);
	}*/
}
