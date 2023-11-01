package org.processmining.localityawareplacenetoracle.models;

public class DMOutput {
    public double[][] ddmMatrix;
    public double[][] edmMatrix;
    public double[][] edmWeightedMatrix;

    public DMOutput(double[][] ddmMatrix, double[][] edmMatrix, double[][] edmWeightedMatrix) {
        this.ddmMatrix = ddmMatrix;
        this.edmMatrix = edmMatrix;
        this.edmWeightedMatrix = edmWeightedMatrix;
    }
}