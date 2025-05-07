package Java_write.Util;

public class Matrix {
    private double[][] data;
    private int[] dim;

    public Matrix(double[][] data) {
        this.data = data;
        this.dim = new int[] {data.length, data[0].length};
    }
    
    public Matrix() {
    }

    public int[] dim() {
        return this.dim;
    }

    public void fromMult(Matrix mat1, Matrix mat2) {
        //mxn nxp
        if (mat1.dim()[1] != mat2.dim()[0]) {
            throw new IllegalArgumentException("matrix dimensions are wrong");
        }
        data = new double[mat1.dim()[0]][mat2.dim()[1]];

        // cij = sum (aik, bkj)
        for (int i=0; i<data.length; i++) {
            for (int j=0; j<data[0].length; j++) {
                double c;

            }
        }
    }
    private double rowMult(double[] i, double[] j) {
        double c = 0;
        for (int n=0; n < i.length; i++) {
            c += idouble * 
        }
    }
}
