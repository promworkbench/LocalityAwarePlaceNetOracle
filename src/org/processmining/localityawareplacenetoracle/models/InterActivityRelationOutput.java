package org.processmining.localityawareplacenetoracle.models;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.processmining.localityawareplacenetoracle.help.Pair;

public class InterActivityRelationOutput {
	private Set<Integer> activityIndices;
	private int transitionsQuantity;
	private HashMap<Pair, Double> dfMatrix;
	private HashMap<Pair, Double> efMatrix;
	private HashMap<Pair, Double> efWeightedMatrix;


	public InterActivityRelationOutput() {
		this.dfMatrix = new HashMap<>();
		this.efMatrix = new HashMap<>();
		this.efWeightedMatrix = new HashMap<>();
		this.activityIndices = new HashSet<>();
		this.transitionsQuantity = 0;
	}

	public void addIndex(int index) {
		this.activityIndices.add(index);
	}

	public int getTransitionsQuantity() {
		return transitionsQuantity;
	}

	public void addTransition() {
		this.transitionsQuantity++;
	}

	public Set<Integer> getActivityIndices() {
		return activityIndices;
	}

	public HashMap<Pair, Double> getDfMatrix() {
		return dfMatrix;
	}

	public void setDfMatrix(HashMap<Pair, Double> dfMatrix) {
		this.dfMatrix = dfMatrix;
	}

	public HashMap<Pair, Double> getEfMatrix() {
		return efMatrix;
	}

	public void setEfMatrix(HashMap<Pair, Double> efMatrix) {
		this.efMatrix = efMatrix;
	}

	public HashMap<Pair, Double> getEfWeightedMatrix() {
		return efWeightedMatrix;
	}

	public void setEfWeightedMatrix(HashMap<Pair, Double> efWeightedMatrix) {
		this.efWeightedMatrix = efWeightedMatrix;
	}

}
