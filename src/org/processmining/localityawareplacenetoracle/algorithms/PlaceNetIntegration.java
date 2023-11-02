package org.processmining.localityawareplacenetoracle.algorithms;
import java.util.HashSet;
import java.util.Set;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

public class PlaceNetIntegration {

    public static PlaceSet integratePlaceNets(Set<Place>Pseq, Set<Place> Pchoice, boolean minSet) {
    	Set<Place> Pfinal = new HashSet<>();

        if (!minSet) {
            Pfinal.addAll(Pseq);
            Pfinal.addAll(Pchoice);
        } else {
            // Removing specific elements from Pseq
        	Set<Place> toRemove = new HashSet<>();
            for (Place pc : Pchoice) {
                for (Place ps : Pseq) {
                    if (areInputOutputTransitionsContained(pc, ps)) {
                    	toRemove.add(ps);
                    }
                }
            }
            Pseq.removeAll(toRemove);

            // Removing specific elements from Pchoice
            toRemove = new HashSet<>();
            for (Place pc1 : Pchoice) {
            	Place reverse = createPlaceWithSwitchedTransitions(pc1);
                if (Pchoice.contains(reverse)) {
                    toRemove.add(pc1);
                    toRemove.add(reverse);
                }
            }
            Pchoice.removeAll(toRemove);

            Pfinal.addAll(Pseq);
            Pfinal.addAll(Pchoice);
        }

        return new PlaceSet(Pfinal);
    }

    public static Place createPlaceWithSwitchedTransitions(Place originalPlace) {
        Place newPlace = new Place();

        // Switch input and output transitions
        for (Transition t : originalPlace.getInputTransitions()) {
            newPlace.addOutputTransition(t);
        }
        for (Transition t : originalPlace.getOutputTransitions()) {
            newPlace.addInputTransition(t);
        }

        // Copy other properties (if needed)
        newPlace.setNumTokens(originalPlace.getNumTokens());
        newPlace.setFinal(originalPlace.isFinal());
        // If there are other properties that need to be copied, do that here.

        return newPlace;
    }
    
    public static boolean areInputOutputTransitionsContained(Place pc, Place ps) {
        if (ps.getInputTransitions().size() != 1 || ps.getOutputTransitions().size() != 1) {
            throw new IllegalArgumentException("ps must have exactly one input transition and one output transition.");
        }

        // Retrieve the single input and output transitions of ps
        Transition psInputTransition = ps.getInputTransitions().iterator().next();
        Transition psOutputTransition = ps.getOutputTransitions().iterator().next();

        // Check if pc's input transitions contain ps's input transition and 
        // pc's output transitions contain ps's output transition
        return pc.isInputTransition(psInputTransition) && pc.isOutputTransition(psOutputTransition);
    }

}