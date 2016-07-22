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
	static final int NUM_OF_AUTHORS = 300;
	static Map<Integer, String> authors = new HashMap<>();
	static Map<Integer, Integer> authorIndices = new HashMap<>();
	static SparseMatrix adjMatrix = SparseMatrix.Factory.zeros(NUM_OF_AUTHORS, NUM_OF_AUTHORS);
	
	public static void loadMatrix(String filename) {
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String[] nums;
        int row = 0;
        
        while (sc.hasNextLine()) {
        	nums = sc.nextLine().split(",");

        	for (int col = 0; col < nums.length; col++) {
        		double val = Double.parseDouble(nums[col]);
        		if (val != 0.0) {
        			adjMatrix.setAsDouble(val, row, col);
        		}
        	}

        	row++;
        }
        sc.close();
    }
	
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
		double max = r.getMaxValue() < epsilon ? 0 : r.getMaxValue();
		double min = r.getMinValue() < epsilon ? 0 : r.getMinValue();
		
		if (max - min == 0) {
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
		int 	exp_f = expand_factor  != null? expand_factor  : 2;
		double 	inf_f = inflate_factor != null? inflate_factor : 2;
		double 	mul_f = mult_factor    != null? mult_factor    : 2;
		int 	max_l = max_loops 	   != null? max_loops 	   : 60;
		double 	eps   = epsilon 	   != null? epsilon 	   : 0.0000000001;
		
		m = selfLoop(m, mul_f);
		m = normalize(m);

		for (int i = 0; i < max_l; i++) {
			m = inflate(m, inf_f);
			m = expand(m, exp_f);
			
			if (stop(m, eps)) {
				break;
			}
		}
		
		return getClusters(m);
	}
	
	public static void main(String[] args) {
		final String FILENAME = "data\\example.csv";
		PrintWriter writer;
		long startTime, endTime;
		
		try {
            writer = new PrintWriter("data\\example_clusters.txt", "UTF-8");;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
		
		startTime = System.currentTimeMillis();
		loadMatrix(FILENAME);
		
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
