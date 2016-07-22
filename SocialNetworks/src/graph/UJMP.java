package graph;

import java.util.ArrayList;
import java.util.List;

import org.ujmp.core.*;
import org.ujmp.core.calculation.Calculation;
//import org.ujmp.core.util.MathUtil;

public class UJMP {

	public static SparseMatrix normalize(SparseMatrix m) {
		Matrix s = m.sum(Calculation.NEW, 0, false); // column-wise sum
		SparseMatrix r = SparseMatrix.Factory.zeros(m.getSize(0), m.getSize(1));
		for (int col = 0; col < m.getSize(1); col++) {
			for (int row = 0; row < m.getSize(0); row++) {
				r.setAsDouble(m.getAsDouble(row, col)/s.getAsDouble(0, col), row, col);
			}
		}
		
		return r;
	}
	
	public static List<List<Integer>> getClusters(SparseMatrix m) {
		List<List<Integer>> clusters = new ArrayList<>();
		List<Matrix> rows = m.getRowList();
		
		for (Matrix row : rows) {
			List<Integer> cluster = new ArrayList<>();
			if (row != null) {
				Matrix val = row.gt(Calculation.NEW, 0.0);
								
				for (int i = 0; i < val.getSize(1); i++) {
					if (val.getAsBoolean(0, i) == true) {
						cluster.add(i);
					}
				}
				clusters.add(cluster);
			}
		}
		
		return clusters;
	}
	
	public static void main(String[] args) {
		// create a very large sparse matrix
		SparseMatrix m = SparseMatrix.Factory.zeros(5, 4);
		System.out.println(m);
		m.setAsDouble(0.5, 0, 0);
		m.setAsDouble(0.25, 1, 0);
		m.setAsDouble(0.25, 0, 1);
		m.setAsDouble(0.5, 3, 2);
		m.setAsDouble(0.1, 3, 3);
		m.setAsDouble(0.7, 4, 3);
		System.out.println(m);
		
		List<List<Integer>> clusters = getClusters(m);
		int i = 1;
		for (List<Integer> cluster : clusters) {
			System.out.print("Cluster " + i++ + ": [");
			for (Integer e : cluster) {
				System.out.print(e + " ");
			}
			System.out.print("]");
			System.out.println();
		}
		
		/*Matrix s = normalize(m);
		System.out.println(s.sum(Calculation.NEW, 0, false));*/
		
		System.out.println("Done");
	}

}
