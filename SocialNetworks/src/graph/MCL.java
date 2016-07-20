package graph;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.ujmp.core.*;
import org.ujmp.core.calculation.Calculation;

public class MCL {
	static final int NUM_OF_AUTHORS = 5000;
	static Map<Integer, String> authors = new HashMap<>();
	static Map<Integer, Integer> authorIndices = new HashMap<>();
	static SparseMatrix adjMatrix = SparseMatrix.Factory.zeros(NUM_OF_AUTHORS, NUM_OF_AUTHORS);
		
	public static void loadMatrix(SparseMatrix m, String filename) {
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // Iterate over the lines in the file, adding new
        // vertices as they are found and connecting them with edges.
        while (sc.hasNextInt()) {
            int i = sc.nextInt();
            int j = sc.nextInt();
            
            // graph is undirected, hence matrix is symmetrical
            m.setAsDouble(1, i, j);
            m.setAsDouble(1, j, i);
        }
        
        sc.close();
    }
	
	/*public static void loadDefinition(Map m, String filename) {
		return;
	}*/
	
	public SparseMatrix normalize(SparseMatrix m) {
		Matrix s = m.sum(Calculation.NEW, 0, false); // column-wise sum
		SparseMatrix r = SparseMatrix.Factory.zeros(NUM_OF_AUTHORS, NUM_OF_AUTHORS);
		for (int col = 0; col < NUM_OF_AUTHORS; col++) {
			for (int row = 0; row < NUM_OF_AUTHORS; row++) {
				r.setAsDouble(m.getAsDouble(row, col)/s.getAsDouble(0, col), row, col);
			}
		}
		
		return r;
	}
	
	public SparseMatrix inflate(SparseMatrix m, double inflate_factor) {
		return normalize((SparseMatrix) m.power(Calculation.ORIG, inflate_factor));
	}
	
	public SparseMatrix expand(SparseMatrix m, int expand_factor) {
		for (int i = 0; i < expand_factor; i++) {
			m = (SparseMatrix)m.times(m);
		}
		return m;
	}
	
	public SparseMatrix selfLoop(SparseMatrix m, double mult_factor) {
		return (SparseMatrix) m.plus(SparseMatrix.Factory.eye(NUM_OF_AUTHORS).times(mult_factor));
	}
	
	public boolean stop(SparseMatrix m, double epsilon) {
		SparseMatrix r = (SparseMatrix)m.times(m).minus(m);
		
		if (Math.abs(r.getMaxValue() - r.getMinValue()) < epsilon) {
			return true;
		}
		
		return false;
	}
	
	public static void main() {
		final String FILENAME = "data\\DBLP_APA.txt";
		
		loadMatrix(adjMatrix, FILENAME);

		System.out.println(adjMatrix);
	}
}
