package org.processmining.localityawareplacenetoracle;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

import org.processmining.localityawareplacenetoracle.parameters.MyParameters;
import org.processmining.localityawareplacenetoracle.dialogs.MyDialog;


public class Main {

		@Plugin(
			name = "PLAPO",
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
			return AlphaLocalDiscovery(context, log, parameters);
        }
        
		
		
		@PluginVariant(variantLabel = "Place Oracle", requiredParameterLabels = { 0, 1 })
        @UITopiaVariant(
                affiliation = "RWTH", 
                author = "Narek Gevorgyan", 
                email = "narek.gevorgyan@rwth-aachen.de"
        )
        public static PlaceSet LocalityAwarePlaceNetOracle(PluginContext context, XLog log, MyParameters params) {//, XEventClassifier classifier) {
        	// fix starttime
			long startTime = System.nanoTime();
			
        	
        	int localWindowSize = params.getContextWindowSize();
			int choiceComplexity = params.getChoicePNComplexity();
			
			double sequenceThreshold = params.getSequenceThreshold();
			double choiceThreshold = params.getChoiceThreshold();
			
			boolean minimalPlaces = params.isMinimalPlaces();
			
			MyParameters.SequenceMatrix sequenceMatrix = params.getSequenceMatrix();
			MyParameters.ChoiceMatrix choiceMatrix = params.getChoiceMatrix();
			MyParameters.Normalization normalizationApproach = params.getNormalizationApproach();
			

			XEventClassifier classifier = log.getClassifiers().get(0);
			XEventClasses classes = XEventClasses.deriveEventClasses(classifier, log);
            
        	Set<Place> places = new HashSet<Place>();
        	


			///     sequence relation parameters
//        	double[][] seq_matrix = new double[classes.size()][classes.size()];
//        	double divider_seq = eventsQuantity;
//        	
//        	if (normCoeff.equals("max")) {
//        		divider_seq = 0;
//        	}
//        	
//        	// add sequence places 
//        	switch(sequenceApproach) {
//                case "DF matrix":
//                	if (divider_seq == 0) {
//                		divider_seq = maxDFMatrix; 
//                	}
//                	seq_matrix = DFMatrix;
//                	break;                
//                case "EF matrix":
//                	if (divider_seq == 0) {
//                		divider_seq = maxEFMatrix; 
//                	} 
//                	seq_matrix = EFMatrix;
//                	break;
//                case "EF matrix weighted":
//                	if (divider_seq == 0) {
//                		divider_seq = maxEFMatrixWeighted; 
//                	} 
//                	seq_matrix = EFMatrixWeighted;                	
//                	break;
//                case "HM matrix":  
//                	seq_matrix = HMMatrix;
//                	divider_seq = 1.0;
//                	break;
//                case "HM matrix based on EF":  
//                	seq_matrix = HMMatrixEF;             
//                	divider_seq = 1.0;
//                	break;
//                case "HM matrix based on EF weighted":  
//                	seq_matrix = HMMatrixWeighted;
//                	divider_seq = 1.0;
//                	break;
//        	}
//
//			///     choice relation parameters
//        	double[][] choice_matrix = new double[classes.size()][classes.size()];
//        	double divider_choice = eventsQuantity;
//        	
//        	if (normCoeff.equals("max")) {
//        		divider_choice = 0;
//        	}
//        	
//        	// add sequence places 
//        	switch(choiseApproach) {
//                case "DF matrix":
//                	if (divider_choice == 0) {
//                		divider_choice = maxDFMatrix; 
//                	}
//                	choice_matrix = DFMatrix;
//                	break;                
//                case "EF matrix":
//                	if (divider_choice == 0) {
//                		divider_choice = maxEFMatrix; 
//                	} 
//                	choice_matrix = EFMatrix;
//                	break;
//                case "EF matrix weighted":
//                	if (divider_choice == 0) {
//                		divider_choice = maxEFMatrixWeighted; 
//                	} 
//                	choice_matrix = EFMatrixWeighted;                	
//                	break;
//                case "HM matrix":  
//                	choice_matrix = HMMatrix;
//                	divider_choice = 1.0;
//                	break;
//                case "HM matrix based on EF":  
//                	choice_matrix = HMMatrixEF;             
//                	divider_choice = 1.0;
//                	break;
//                case "HM matrix based on EF weighted":  
//                	choice_matrix = HMMatrixWeighted;
//                	divider_choice = 1.0;
//                	break;
//        	}
//
//        	

        	
        	long delta = System.nanoTime() - startTime;
        	System.out.println("===========================================================");
        	System.out.println("Number of places: " + places.size());
        	System.out.println("Runtime " + delta / 1e9 + "S");
        	System.out.println("===========================================================");
        	return new PlaceSet(places);
			
        }