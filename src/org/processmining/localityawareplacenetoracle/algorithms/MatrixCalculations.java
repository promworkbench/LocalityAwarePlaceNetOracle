package org.processmining.localityawareplacenetoracle.algorithms;

import java.util.HashMap;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.localityawareplacenetoracle.help.Pair;
import org.processmining.localityawareplacenetoracle.models.DEFOutput;
import org.processmining.localityawareplacenetoracle.models.DMOutput;
import org.processmining.localityawareplacenetoracle.models.InterActivityRelationOutput;

public class MatrixCalculations {

    /**
     * Initializes a square matrix of given size with all elements as zero.
     *
     * @param size The size of the matrix.
     * @return A square matrix with all elements set to zero.
     */
    public static double[][] initializeMatrix(int size) {
        return new double[size][size];
    }


    public static DEFOutput calculateDEFMatrix(XLog log, XEventClasses classes, int contextWindowSize) {
        
    	double[][] dfMatrix = initializeMatrix(classes.size());
        double[][] efMatrix = initializeMatrix(classes.size());
        double[][] efWeightedMatrix = initializeMatrix(classes.size());

        double maxDFMatrix = 0.0;
        double maxEFMatrix = 0.0;
        double maxEFWeightedMatrix = 0.0;

        for (XTrace trace : log) {
            if (!trace.isEmpty()) {
                for (int start_ind = 0; start_ind < trace.size() - 1; start_ind++) {
                    InterActivityRelationOutput localDEFMatrrix = calculateLocalDEFMatrrix(trace, start_ind, contextWindowSize, classes);
                    for (Pair index : localDEFMatrrix.getDfMatrix().keySet()) {
                        int index1 = index.getIndex1();
                        int index2 = index.getIndex2();
                        		
                    	if (localDEFMatrrix.getDfMatrix().get(index) != null) {
                            dfMatrix[index1][index2] += localDEFMatrrix.getDfMatrix().get(index);
                        }

                        efMatrix[index1][index2] += localDEFMatrrix.getEfMatrix().get(index);
                        efWeightedMatrix[index1][index2] += localDEFMatrrix.getEfWeightedMatrix().get(index);

                        maxDFMatrix = Math.max(maxDFMatrix, dfMatrix[index1][index2]);
                        maxEFMatrix = Math.max(maxEFMatrix, efMatrix[index1][index2]);
                        maxEFWeightedMatrix = Math.max(maxEFWeightedMatrix, efWeightedMatrix[index1][index2]);
                    }

                }

            }
        }
            
        DEFOutput result = new DEFOutput(dfMatrix, maxDFMatrix, efMatrix, maxEFMatrix, efWeightedMatrix, maxEFWeightedMatrix);
        return result;

    }

    /**
     * Calculates the Heuristic Miner-like matrix.
     *
     * @param dfMatrix The Directly Follows matrix.
     * @return The Heuristic Miner-like matrix.
     */
    public static DMOutput calculateDMmatrix(DEFOutput DEFMatrix) {
        
    	double[][] dfMatrix = DEFMatrix.dfMatrix;
    	double[][] efMatrix = DEFMatrix.efMatrix;
    	double[][] efWeightedMatrix = DEFMatrix.efWeightedMatrix;
    	
    	double[][] ddmMatrix = new double[dfMatrix.length][dfMatrix.length];
    	double[][] edmMatrix = new double[dfMatrix.length][dfMatrix.length];
    	double[][] edmWeightedMatrix = new double[dfMatrix.length][dfMatrix.length];
    	


    	for (int i = 0; i < efMatrix[0].length; i++) {
    		for (int j = 0; j <= i; j++) {
    			if (i == j) {
    				ddmMatrix[i][j] = dfMatrix[i][j] / (dfMatrix[i][j] + 1);
    				edmMatrix[i][j] = efMatrix[i][j] / (efMatrix[i][j] + 1);
    				efWeightedMatrix[i][j] = edmWeightedMatrix[i][j] / (edmWeightedMatrix[i][j] + 1);
    			} else {
    				//HMMatrix[i][j] = (java.lang.Math.abs(DFMatrix[i][j]) - java.lang.Math.abs(DFMatrix[j][i])) / (java.lang.Math.abs(DFMatrix[i][j]) + java.lang.Math.abs(DFMatrix[j][i]) + 1);
    				ddmMatrix[i][j] = (dfMatrix[i][j] - dfMatrix[j][i]) / (dfMatrix[i][j] + dfMatrix[j][i] + 1);
    				ddmMatrix[j][i] = - ddmMatrix[i][j];
    				
    				//HMMatrixEF[i][j] = (java.lang.Math.abs(EFMatrix[i][j]) - java.lang.Math.abs(EFMatrix[j][i])) / (java.lang.Math.abs(EFMatrix[i][j]) + java.lang.Math.abs(EFMatrix[j][i]) + 1);
    				edmMatrix[i][j] = (efMatrix[i][j] - efMatrix[j][i]) / (efMatrix[i][j] + efMatrix[j][i] + 1);
    				edmMatrix[j][i] = - edmMatrix[i][j];
    				
    				//HMMatrixWeighted[i][j] = (java.lang.Math.abs(EFMatrixWeighted[i][j]) - java.lang.Math.abs(EFMatrixWeighted[j][i])) / (java.lang.Math.abs(EFMatrixWeighted[i][j]) + java.lang.Math.abs(EFMatrixWeighted[j][i]) + 1);
    				edmWeightedMatrix[i][j] = (edmWeightedMatrix[i][j] - edmWeightedMatrix[j][i]) / (edmWeightedMatrix[i][j] + edmWeightedMatrix[j][i] + 1);
    				edmWeightedMatrix[j][i] = - edmWeightedMatrix[i][j];
    			}
    		
    		}
    	}
    	
    	return new DMOutput(ddmMatrix, edmMatrix, edmWeightedMatrix);
    }

    public static InterActivityRelationOutput calculateLocalDEFMatrrix(XTrace trace, int startIndex, int contexSize, XEventClasses classes) {
        // derive DFA for a specific subtrace

        HashMap<Pair, Double> dfMatrix = new HashMap<>();
        HashMap<Pair, Double> efMatrix = new HashMap<>();
        HashMap<Pair, Double> efWeightedMatrix = new HashMap<>();

        InterActivityRelationOutput results = new InterActivityRelationOutput();
        int maxStep = Math.min(trace.size() - startIndex - 1, contexSize - 1);

        for (int step = 1; step <= maxStep; step++) {
            XEventClass from = classes.getClassOf(trace.get(startIndex));
            XEventClass to = classes.getClassOf(trace.get(startIndex + step));
            Pair index = new Pair(from.getIndex(), to.getIndex());

            if (step == 1) {
                Double dfCount = dfMatrix.get(index);
                if (dfCount == null) dfCount = 0.0;
                dfMatrix.put(index, dfCount + 1.0);
            }

            Double efWeightedCount = efWeightedMatrix.get(index);
            if (efWeightedCount == null) efWeightedCount = 0.0;
            efWeightedMatrix.put(index, efWeightedCount + 1.0 / step);

            Double efCount = efMatrix.get(index);
            if (efCount == null) efCount = 0.0;
            efMatrix.put(index, efCount + 1.0);

            results.addTransition();
        }

        results.setDfMatrix(dfMatrix);
        results.setEfMatrix(efMatrix);
        results.setEfWeightedMatrix(efWeightedMatrix);
        return results;
    }

}
