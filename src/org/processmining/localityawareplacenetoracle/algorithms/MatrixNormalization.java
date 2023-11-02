package org.processmining.localityawareplacenetoracle.algorithms;
import java.util.Arrays;

import org.processmining.localityawareplacenetoracle.parameters.MyParameters;


public class MatrixNormalization {
    
	public static double[][] normalizeMatrixMinMax(double[][] matrix) {
	        double min = Double.MAX_VALUE;
	        double max = Double.MIN_VALUE;
	
	        // Find the min and max values in the matrix
	        for (double[] row : matrix) {
	            for (double value : row) {
	                if (value < min) {
	                    min = value;
	                }
	                if (value > max) {
	                    max = value;
	                }
	            }
	        }
	
	        // Check if max and min are the same (to avoid division by zero)
	        if (max == min) {
	            return matrix;  // Or throw an exception or handle this case as per requirement
	        }
	
	        double[][] normalizedMatrix = new double[matrix.length][];
	        for (int i = 0; i < matrix.length; i++) {
	            normalizedMatrix[i] = new double[matrix[i].length];
	            for (int j = 0; j < matrix[i].length; j++) {
	                normalizedMatrix[i][j] = (matrix[i][j] - min) / (max - min);
	            }
	        }
	
	        return normalizedMatrix;
	    }
	
    
	public static double[][] normalizeMatrixLogMinMax(double[][] matrix) {
        // Apply log transformation and find min, max
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double[][] logTransformedMatrix = new double[matrix.length][];

        for (int i = 0; i < matrix.length; i++) {
            logTransformedMatrix[i] = new double[matrix[i].length];
            for (int j = 0; j < matrix[i].length; j++) {
                // Ensure the value is positive for log transformation
                if (matrix[i][j] < 0) {
                    throw new IllegalArgumentException("Matrix elements must be non-negative for log transformation.");
                }
                logTransformedMatrix[i][j] = Math.log(matrix[i][j] + 1);

                if (logTransformedMatrix[i][j] < min) {
                    min = logTransformedMatrix[i][j];
                }
                if (logTransformedMatrix[i][j] > max) {
                    max = logTransformedMatrix[i][j];
                }
            }
        }

        // Normalize the log-transformed matrix
        if (max == min) {
            return logTransformedMatrix; // handle this as needed
        }

        double[][] normalizedMatrix = new double[matrix.length][];
        for (int i = 0; i < logTransformedMatrix.length; i++) {
            normalizedMatrix[i] = new double[logTransformedMatrix[i].length];
            for (int j = 0; j < logTransformedMatrix[i].length; j++) {
                normalizedMatrix[i][j] = (logTransformedMatrix[i][j] - min) / (max - min);
            }
        }

        return normalizedMatrix;
    }
	
    public static double[][] normalizeMatrixWinsorizeMinMax(double[][] matrix, double lowerPercentile, double upperPercentile) {
        int totalElements = 0;
        for (double[] row : matrix) {
            totalElements += row.length;
        }

        double[] sortedElements = new double[totalElements];
        int index = 0;
        for (double[] row : matrix) {
            for (double value : row) {
                sortedElements[index++] = value;
            }
        }
        Arrays.sort(sortedElements);

        double lowerValue = sortedElements[(int) (totalElements * lowerPercentile / 100.0)];
        double upperValue = sortedElements[(int) (totalElements * upperPercentile / 100.0) - 1]; // -1 because of 0 indexing

        // Apply Winsorization
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] < lowerValue) {
                    matrix[i][j] = lowerValue;
                } else if (matrix[i][j] > upperValue) {
                    matrix[i][j] = upperValue;
                }
            }
        }

        // Apply min-max normalization
        return normalizeMatrixMinMax(matrix);
    }
    
    public static double[][] normalize(double[][] matrix, MyParameters.Normalization approach) {
    	switch (approach) {
    		case MINMAX:
    			return normalizeMatrixMinMax(matrix);
    		case LOG:
    			return normalizeMatrixLogMinMax(matrix);
    		case WINSOR:
    			return normalizeMatrixWinsorizeMinMax(matrix, 5, 95);
    	}
    	return normalizeMatrixMinMax(matrix);
    }


}