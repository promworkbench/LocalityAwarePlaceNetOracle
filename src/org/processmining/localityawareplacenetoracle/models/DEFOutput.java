package org.processmining.localityawareplacenetoracle.models;

public class DEFOutput {
    public double[][] dfMatrix;
    public double maxDFMatrix;
    public double[][] efMatrix;
    public double maxEFMatrix;
    public double[][] efWeightedMatrix;
    public double maxEFWeightedMatrix;

    public DEFOutput(double[][] dfMatrix, double maxDFMatrix, double[][] efMatrix, double maxEFMatrix, double[][] efWeightedMatrix, double maxEFWeightedMatrix) {
        this.dfMatrix = dfMatrix;
        this.maxDFMatrix = maxDFMatrix;
        this.efMatrix = efMatrix;
        this.maxEFMatrix = maxEFMatrix;
        this.efWeightedMatrix = efWeightedMatrix;
        this.maxEFWeightedMatrix = maxEFWeightedMatrix;
    }
}