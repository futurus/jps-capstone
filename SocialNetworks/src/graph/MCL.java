package graph;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
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
        String[] nums;
        
        while (sc.hasNextLine()) {
        	nums = sc.nextLine().split("\t");
            int i = Integer.parseInt(nums[0]);
            int j = Integer.parseInt(nums[1]);

            // graph is undirected, hence matrix is symmetrical
            m.setAsDouble(1, i, j);
            m.setAsDouble(1, j, i);
        }
        
        sc.close();
    }
	
	/*public static void loadDefinition(Map m, String filename) {
		return;
	}*/
	
	public static SparseMatrix normalize(SparseMatrix m) {
		Matrix s = m.sum(Calculation.NEW, 0, false); // column-wise sum
		SparseMatrix r = SparseMatrix.Factory.zeros(NUM_OF_AUTHORS, NUM_OF_AUTHORS);
		for (int col = 0; col < NUM_OF_AUTHORS; col++) {
			for (int row = 0; row < NUM_OF_AUTHORS; row++) {
				if (s.getAsDouble(0, col) != 0) {
					r.setAsDouble(m.getAsDouble(row, col)/s.getAsDouble(0, col), row, col);
				}
			}
		}
		
		return r;
	}
	
	public static SparseMatrix inflate(SparseMatrix m, double inflate_factor) {
		return (SparseMatrix) m.power(Calculation.ORIG, inflate_factor);
	}
	
	public static SparseMatrix expand(SparseMatrix m, int expand_factor) {
		for (int i = 0; i < expand_factor; i++) {
			m = (SparseMatrix) m.times(m);
		}
		return m;
	}
	
	public static SparseMatrix selfLoop(SparseMatrix m, double mult_factor) {
		return (SparseMatrix) m.plus(SparseMatrix.Factory.eye(NUM_OF_AUTHORS, NUM_OF_AUTHORS).times(mult_factor));
	}
	
	public static boolean stop(SparseMatrix m, double epsilon) {
		SparseMatrix r = (SparseMatrix)m.times(m).minus(m);
		
		if (Math.abs(r.getMaxValue() - r.getMinValue()) < epsilon) {
			return true;
		}
		
		return false;
	}
	
	public static List<List<Integer>> getClusters(SparseMatrix m) {
		List<List<Integer>> clusters = new ArrayList<>();
		List<Matrix> rows = m.getRowList();
		
		for (Matrix row : rows) {
			List<Integer> cluster = new ArrayList<>();
			if (row != null) {
				Matrix val = row.gt(Calculation.NEW, 0.0);
				
				for (int i = 0; i < NUM_OF_AUTHORS; i++) {
					if (val.getAsBoolean(0, i) == true) {
						cluster.add(i);
					}
				}
				clusters.add(cluster);
			}
		}
		
		return clusters;
	}
	
	public static List<List<Integer>> mcl(SparseMatrix m, 
										  Integer expand_factor, 
										  Double inflate_factor, 
										  Double mult_factor, 
										  Integer max_loops, 
										  Double epsilon) {
		int 	exp_f = expand_factor  != null? expand_factor  : 5;
		double 	inf_f = inflate_factor != null? inflate_factor : 5;
		double 	mul_f = mult_factor    != null? mult_factor    : 2;
		int 	max_l = max_loops 	   != null? max_loops 	   : 10000;
		double 	eps   = epsilon 	   != null? epsilon 	   : 0.000001;
		
		m = selfLoop(m, mul_f);
		m = normalize(m);
		
		for (int i = 0; i < max_l; i++) {
			m = inflate(m, inf_f);
			m = expand(m, exp_f);
			
			if (stop(m, eps)) break;
		}
		
		return getClusters(m);
	}
	
	public static void main(String[] args) {
		final String FILENAME = "data\\DBLP_APA.txt";
		PrintWriter writer;
		long startTime, endTime;
		
		try {
            writer = new PrintWriter("data\\DBLP_clusters.txt", "UTF-8");;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
		
		startTime = System.currentTimeMillis();
		loadMatrix(adjMatrix, FILENAME);
		endTime   = System.currentTimeMillis();
		writer.print("Done reading file: ");
		writer.println(endTime - startTime);
		
		startTime = endTime;
		List<List<Integer>> clusters = mcl(adjMatrix, null, null, null, null, null);
				
		endTime   = System.currentTimeMillis();
		writer.print("Done finding clusters: ");
		writer.println(endTime - startTime);
		
		startTime = endTime;
		int i = 1;
		for (List<Integer> cluster : clusters) {
			writer.print("Cluster " + i++ + ": [");
			for (Integer e : cluster) {
				writer.print(e + " ");
			}
			writer.print("]\n");
		}
		endTime = System.currentTimeMillis();
		writer.print("Done output: ");
		writer.println(endTime - startTime);
		writer.close();
	}
}
