package org.processmining.localityawareplacenetoracle;


import java.util.HashSet;
import java.util.Set;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.localityawareplacenetoracle.algorithms.ChoicePlaceNetDiscovery;
import org.processmining.localityawareplacenetoracle.algorithms.MatrixCalculations;
import org.processmining.localityawareplacenetoracle.algorithms.MatrixNormalization;
import org.processmining.localityawareplacenetoracle.algorithms.PlaceNetIntegration;
import org.processmining.localityawareplacenetoracle.algorithms.SequencePlaceNetDiscovery;
import org.processmining.localityawareplacenetoracle.dialogs.MyDialog;
import org.processmining.localityawareplacenetoracle.models.DEFOutput;
import org.processmining.localityawareplacenetoracle.models.DMOutput;
import org.processmining.localityawareplacenetoracle.parameters.MyParameters;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;


public class Main {

		@Plugin(
			name = "Locality Aware Place Net Oracle",
			parameterLabels = {},
			returnLabels = { "Set of Place Nets" },
			returnTypes = { PlaceSet.class },
			userAccessible = true,
			help = "Plugin for discovering place nets given a log and parameters"
        )
        @UITopiaVariant(
			affiliation = "RWTH",
			author = "Narek Gevorgyan",
			email = "narek.gevorgyan@rwth-aachen.de"
        )
        public static PlaceSet AlphaLocalDiscovery(UIPluginContext context, XLog log) {
			MyParameters parameters = new MyParameters();
			MyDialog dialog = new MyDialog(parameters);
			InteractionResult result = context.showWizard("Configure Place Oracle", true, true, dialog);
			if (result != InteractionResult.FINISHED) {
				return null;
			}
			return LocalityAwarePlaceNetOracle(context, log, parameters);
        }
        
		
		
		@PluginVariant(variantLabel = "Place Oracle", requiredParameterLabels = { 0, 1 })
        @UITopiaVariant(
                affiliation = "RWTH", 
                author = "Narek Gevorgyan", 
                email = "narek.gevorgyan@rwth-aachen.de"
        )
        public static PlaceSet LocalityAwarePlaceNetOracle(PluginContext context, XLog log, MyParameters params) {
			
			long startTime = System.nanoTime();
			
        	
        	int contextWindowSize = params.getContextWindowSize();
			int choiceComplexity = params.getChoicePNComplexity();
			
			double sequenceThreshold = params.getSequenceThreshold();
			double choiceThreshold = params.getChoiceThreshold();
			
			boolean minimalPlaces = params.isMinimalPlaces();
			
			MyParameters.SequenceMatrix sequenceApproach = params.getSequenceMatrix();
			MyParameters.ChoiceMatrix choiceApproach = params.getChoiceMatrix();
			MyParameters.Normalization normalizationApproach = params.getNormalizationApproach();
			

			XEventClassifier classifier = log.getClassifiers().get(0);
			XEventClasses classes = XEventClasses.deriveEventClasses(classifier, log);
            
        	Set<Place> places = new HashSet<Place>();
        	
        	
        	// Calculate matrices DF, EF and EF weighted
        	DEFOutput defMatrix =  MatrixCalculations.calculateDEFMatrix(log, classes, contextWindowSize);
        	
        	// Calculate matrices DDM, EDM and EDM weighted
        	DMOutput dmMatrix = MatrixCalculations.calculateDMmatrix(defMatrix);
        	
        	double[][] sequenceMatrix = MatrixCalculations.initializeMatrix(classes.size());
        	double[][] choiceMatrix = MatrixCalculations.initializeMatrix(classes.size());
        	
        	
        	
        	switch (sequenceApproach) {
        		case DF:
        			sequenceMatrix = MatrixNormalization.normalize(defMatrix.dfMatrix, normalizationApproach);
        			break;
        		case EF:
        			sequenceMatrix = MatrixNormalization.normalize(defMatrix.efMatrix, normalizationApproach);
        			break;
        		case DDM:
        			sequenceMatrix = dmMatrix.ddmMatrix;
        			break;
        		case EDM:
        			sequenceMatrix = dmMatrix.edmMatrix;
        			break;
        		case EFWEIGHTED:
        			sequenceMatrix = MatrixNormalization.normalize(defMatrix.efWeightedMatrix, normalizationApproach);
        			break;
        		case EDMWEIGHTED:
        			sequenceMatrix = defMatrix.efWeightedMatrix;		
        	}
        	

 
        	switch (choiceApproach) {
        		case DDM:
        			choiceMatrix = dmMatrix.ddmMatrix;
        			break;
        		case EDM:
        			choiceMatrix = dmMatrix.edmMatrix;
        			break;
        		case EDMWEIGHTED:
        			choiceMatrix = dmMatrix.edmWeightedMatrix;		
        	}
        	
        	Set<Place> sequencePlaceNetSet = SequencePlaceNetDiscovery.discoverSequencePlaceNetSet(sequenceMatrix, sequenceThreshold, log);
        	
        	Set<Place> choicePlaceNetSet;
        	if (choiceComplexity > 2) {
        	    choicePlaceNetSet = ChoicePlaceNetDiscovery.discoverChoicePlaceNetSet(log, choiceComplexity, sequenceMatrix, sequenceThreshold, choiceMatrix, choiceThreshold);
        	} else {
        	    choicePlaceNetSet = new HashSet<>();
        	}
        	
        	PlaceSet resultingPlaceSet = PlaceNetIntegration.integratePlaceNets(sequencePlaceNetSet, choicePlaceNetSet, minimalPlaces);
        	long delta = System.nanoTime() - startTime;
        	System.out.println("===========================================================");
        	System.out.println("Number of places: " + places.size());
        	System.out.println("Runtime " + delta / 1e9 + "S");
        	System.out.println("===========================================================");
        	return resultingPlaceSet;
			
        }
}	