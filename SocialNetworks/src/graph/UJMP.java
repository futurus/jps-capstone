package graph;

import org.ujmp.core.*;
import org.ujmp.core.util.MathUtil;

public class UJMP {

	public static void main(String[] args) {
		// create a very large sparse matrix
		SparseMatrix m1 = SparseMatrix.Factory.zeros(1000000, 500000);

		// set some values
		m1.setAsDouble(MathUtil.nextGaussian(), 0, 0);
		m1.setAsDouble(MathUtil.nextGaussian(), 1, 1);
		for (int i = 0; i < 10000; i++) {
			m1.setAsDouble(MathUtil.nextGaussian(), MathUtil.nextInteger(0, 1000), MathUtil.nextInteger(0, 1000));
		}

		// show on screen
		//m1.showGUI();

		// create another matrix
		SparseMatrix m2 = SparseMatrix.Factory.zeros(3000000, 500000);

		// set some values
		m2.setAsDouble(MathUtil.nextGaussian(), 0, 0);
		m2.setAsDouble(MathUtil.nextGaussian(), 1, 1);
		for (int i = 0; i < 10000; i++) {
			m2.setAsDouble(MathUtil.nextGaussian(), MathUtil.nextInteger(0, 1000), MathUtil.nextInteger(0, 1000));
		}

		// show on screen
		//m2.showGUI();

		// do some operations
		Matrix m3 = m1.mtimes(m2.transpose());

		System.out.println("Done");
		// show on screen
		//m3.showGUI();
	}

}
