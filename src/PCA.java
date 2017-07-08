import Jama.Matrix;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * Created by padma on 7/7/17.
 */
public class PCA {
    public Matrix dataMatrix;

    //constructor PCA should take in rawData.

    /*private void fillDataIntoMatrix(double[][] rawData) {

        //rawData is give as [[x1, x2, x3], [y1, y2, y3], [z1, z2, z3]]
        // fills the Matrix with data with x in first row, y in second, etc.
        Matrix filledMatrix = new Matrix(rawData);

        //rows must represent observations, and columns represent variables
        //now with transpose, the first row has x1 y1 z1 row two has x2 y2 z2 etc
        filledMatrix.transpose();
        dataMatrix = filledMatrix;
    }
    **/

    private double[] getAverages(double[][] rawData) {
        //next, get the averages of each variable and store them in an double[] array
        //remember, rawData.length will give you the number of variables
        double[] meanValues = new double[rawData.length];
        for (int i = 0; i < rawData.length; i++) {
            double meanVal = new DescriptiveStatistics(rawData[i]).getMean();
            meanValues[i] = meanVal;
        }
        return meanValues;
    }

    private double[][] subtractAveragesFromVariables(double[][] rawData, double[] meanValues) {
        //index of meanValues matches the row index of rawData (in terms of matching mean to variable)
        for (int i = 0; i < meanValues.length; i++) {
            //get each mean one by one
            double currentMean = meanValues[i];
            //find the corresponding row, and iterate along its values.
            for (int j = 0; j < rawData[i].length; j++) {
                //subtract mean from each value in the row
                rawData[i][j] = rawData[i][j] - currentMean;
            }
        }
        return rawData;
    }

    private Matrix outputCovarianceMatrix(double[][] subtractedMeanData) {
        int origArrayLen = subtractedMeanData.length;
        Matrix covarianceMatrix = new Matrix(origArrayLen, origArrayLen);
        for ()

    }

    private void subtractMeanFromEachDimension(Matrix inputMatrix) {
        int numberOfColumns = inputMatrix.getColumnDimension();
        int maxRowIndex = inputMatrix.getRowDimension() - 1;
        for (int i = 0; i < numberOfColumns; i++) {
            Matrix singleColumn = inputMatrix.getMatrix(0, maxRowIndex, new int[]{i});


        }





    public Matrix compute(Matrix filledMatrix) {}




}
