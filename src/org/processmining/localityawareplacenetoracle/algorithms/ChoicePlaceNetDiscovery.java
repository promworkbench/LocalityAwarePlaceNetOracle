package org.processmining.localityawareplacenetoracle.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;


public class ChoicePlaceNetDiscovery {

    
    public static Set<Place> discoverChoicePlaceNetSet(XLog log, int k, double[][] seq_matrix, double seq_threshold, double[][] choice_matrix, double choice_threshold) {
        if (k < 3) {
            throw new IllegalArgumentException("k must be at least 3.");
        }
		XEventClassifier classifier = log.getClassifiers().get(0);
		XEventClasses classes = XEventClasses.deriveEventClasses(classifier, log);
		

        Set<Place> places = new HashSet<>();
        generateSetsRecursive(new ArrayList<>(), seq_matrix.length, k, places, seq_matrix, seq_threshold, choice_matrix, choice_threshold, classes);
        return places;
    }
    
    private static void generateSetsRecursive(List<Integer> currentSet, int m, int k, Set<Place> places, double[][] seq_matrix, double seq_threshold, double[][] choice_matrix, double choice_threshold, XEventClasses classes) {
        if (currentSet.size() >= 3) {
            List<List<List<Integer>>> partitions = evaluateCriteria(currentSet, seq_matrix, seq_threshold, choice_matrix, choice_threshold);
            for (List<List<Integer>> partition : partitions) {
                // Create and add new Place based on partition
                Place newPlace = new Place();
                for (int input : partition.get(0)) {
                    newPlace.addInputTransition(new Transition(classes.getByIndex(input).toString(), false));
                }
                for (int output : partition.get(1)) {
                    newPlace.addOutputTransition(new Transition(classes.getByIndex(output).toString(), false));
                }
                
                if (newPlace.getInputTransitions().size() + newPlace.getOutputTransitions().size() > 2) {
                	places.add(newPlace);
                }
            }
        }

        if (currentSet.size() == k) {
            return;
        }

        for (int i = 0; i < m; i++) {
            List<Integer> newSet = new ArrayList<>(currentSet);
            newSet.add(i);
            generateSetsRecursive(newSet, m, k, places, seq_matrix, seq_threshold, choice_matrix, choice_threshold, classes);
        }
    }
    
    private static List<List<List<Integer>>> evaluateCriteria(List<Integer> set, double[][] seq_matrix, double seq_threshold, double[][] choice_matrix, double choice_threshold) {
        int n = set.size();
        List<List<List<Integer>>> satisfyingPartitions = new ArrayList<>();
        
        // Split set into two subsets and evaluate each split
        for (int i = 1; i < n; i++) {
            List<Integer> set1 = new ArrayList<>(set.subList(0, i));
            List<Integer> set2 = new ArrayList<>(set.subList(i, n));

            if (isCriteriaSatisfied(set1, set2, seq_matrix, seq_threshold, choice_matrix, choice_threshold)) {
                satisfyingPartitions.add(Arrays.asList(set1, set2));
            }
        }

        return satisfyingPartitions;
    }

    private static boolean isCriteriaSatisfied(List<Integer> set1, List<Integer> set2, double[][] seq_matrix, double seq_threshold, double[][] choice_matrix, double choice_threshold) {
        // Check sequence criteria
        for (int index1 : set1) {
            for (int index2 : set2) {
                if (seq_matrix[index1][index2] < seq_threshold) {
                    return false;
                }
            }
        }
        
        // Check choice criteria within each set
        return checkChoiceCriteria(set1, choice_matrix, choice_threshold) &&
               checkChoiceCriteria(set2, choice_matrix, choice_threshold);
    }

    private static boolean checkChoiceCriteria(List<Integer> set, double[][] choice_matrix, double choice_threshold) {
        for (int i = 0; i < set.size(); i++) {
            for (int j = i + 1; j < set.size(); j++) {
                int index1 = set.get(i);
                int index2 = set.get(j);
                if (Math.abs(choice_matrix[index1][index2]) > 1 - choice_threshold) {
                    return false;
                }
            }
        }
        return true;
    }
}