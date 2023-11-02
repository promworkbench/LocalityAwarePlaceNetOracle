package org.processmining.localityawareplacenetoracle.algorithms;

import java.util.HashSet;
import java.util.Set;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;


public class SequencePlaceNetDiscovery {
    
	public static Set<Place> discoverSequencePlaceNetSet(double[][] sequenceMatrix, double sequenceThreshold, XLog log) {
		XEventClassifier classifier = log.getClassifiers().get(0);
		XEventClasses classes = XEventClasses.deriveEventClasses(classifier, log);
		
		Set<Place> places = new HashSet<Place>();
		
        for (int i = 0; i < sequenceMatrix.length; i++) {
            for (int j = 0; j < sequenceMatrix[i].length; j++) {
                if (sequenceMatrix[i][j] >= sequenceThreshold) {
    				Place newPlace = new Place();
    				newPlace.addInputTransition(new Transition(classes.getByIndex(i).toString(), false));
    				newPlace.addOutputTransition(new Transition(classes.getByIndex(j).toString(), false));
    				places.add(newPlace);
                }
            }
        }
        
        return places;
	}
	
}
