import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import util.GraphLoader;
import graph.CapGraph;
import graph.Graph;

public class CapGraphTest {

	@Before
	public void setUp() throws Exception {
		Graph graph = new CapGraph();
        GraphLoader.loadGraph(graph, "data/facebook_ucsd.txt");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
