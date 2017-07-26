import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by padma on 7/7/17.
 */
public class PCA {

    //you need two rawData variables; one of which gets modified when the mean in subtracted, and one that is never touched
    //origData is never touched
    private double[][] rawData;
    private double[][] origData;

    //constructor PCA should take in raw data.
    public PCA(double[][] rawdata) {
        rawData = rawdata;
        origData = rawdata;
    }


    //for below, assume rawData is given as [[x1, x2, x3], [y1, y2, y3], [z1, z2, z3]]
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
        //creating empty square covariance matrix
        Matrix covarianceMatrix = new Matrix(origArrayLen, origArrayLen);
        for (int i = 0; i < origArrayLen; i++) {
            for (int j = 0; j < origArrayLen; j++) {
                //i moves slower than j, so x,x then x,y then x,z
                double currCovariance = new Covariance().covariance(subtractedMeanData[i], subtractedMeanData[j]);
                //set into correct place; for example x,y is in 0,1 so i,j corr to 0,1
                covarianceMatrix.set(i, j, currCovariance);
            }
        }
        return covarianceMatrix;
    }

    private double[] getEigenVals(Matrix covarianceMatrix) {
        return new EigenvalueDecomposition(covarianceMatrix).getRealEigenvalues();
    }

    private Matrix getEigenVecs(Matrix covarianceMatrix) {
        return new EigenvalueDecomposition(covarianceMatrix).getV();
    }

    private Matrix orderEigenVecMatrix (double[] eigenVals, Matrix eigenVecs) {
        //turn array into list so that it can be sorted
        ArrayList<Double> eigenValList = new ArrayList<Double>();
        for (int i = 0; i < eigenVals.length; i++) {
            eigenValList.add(eigenVals[i]);
        }
        //sorts from small to big
        Collections.sort(eigenValList);
        //reverses so that we have big to small
        Collections.reverse(eigenValList);

        //new empty eigenvec Matrix
        Matrix orderedEigenVecs = new Matrix(eigenVecs.getRowDimension(), eigenVecs.getColumnDimension());
        //we want horizontal arrays so that the eigenvec will be in rows not col
        double[][] orderedEigenVecsHoriz = orderedEigenVecs.transpose().getArray();

        //get 2D array from original eigenvec Matrix (eigen vec in rows again not cols)
        double [][] origEigenVecsHoriz = eigenVecs.transpose().getArray();

        //go through each orig val and compare with every new val
        for (int i = 0; i < eigenVals.length; i++) {
            for (int j = 0; j < eigenValList.size(); j++) {
                //look for match
                if (eigenVals[i] == eigenValList.get(j)) {
                    orderedEigenVecsHoriz[j] = origEigenVecsHoriz[i];
                    //once a j index number from eigenVal list has been matched, it can't be matched again.
                    // This is to avoid doubles. Therefore, the value is maxed out.
                    eigenValList.set(j, Double.MAX_VALUE);
                }
            }
        }
        //turn the eigenvec matrix back to eigenvec as columns
        orderedEigenVecs = new Matrix(orderedEigenVecsHoriz).transpose();
        return orderedEigenVecs;
    }


    private Matrix outputFinalData(Matrix orderedEigenVecs, double[][] rawData) {
        Matrix origData = new Matrix(rawData);
        Matrix finalData = orderedEigenVecs.transpose().times(origData);
        return finalData;
    }

    public Matrix runPCA() {
        double[][] subtractedData = subtractAveragesFromVariables(rawData, getAverages(rawData));
        Matrix covarianceMatrix = outputCovarianceMatrix(subtractedData);
        Matrix finalData = outputFinalData(orderEigenVecMatrix(getEigenVals(covarianceMatrix),
                getEigenVecs(covarianceMatrix)), origData);
        return finalData;

    }

    public static void main(String[] args) {
        double[][] testData = {{2.5, 0.5, 2.2, 1.9, 3.1, 2.3, 2, 1, 1.5, 1.1},
                {2.4, 0.7, 2.9, 2.2, 3.0, 2.7, 1.6, 1.1, 1.6, 0.9}};
        PCA tester = new PCA(testData);
        double[][] finalData = tester.runPCA().getArray();
        for (int i = 0; i < finalData.length; i++) {
            for (int j = 0; j < finalData[0].length; j++) {
                System.out.println(finalData[i][j]);
            }
        }
    }

}
