package org.processmining.localityawareplacenetoracle.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.processmining.localityawareplacenetoracle.parameters.MyParameters;

import com.fluxicon.slickerbox.colors.SlickerColors;
import com.fluxicon.slickerbox.components.NiceSlider;
import com.fluxicon.slickerbox.components.NiceSlider.Orientation;
import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
 


public class MyDialog extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9048821565595960963L;

	/**
	 * Parameter dialog for converting the given workshop model to a workflow
	 * graph.
	 * 
	 * @param model
	 *            The given workshop model.
	 * @param parameters
	 *            The parameters which will be used for the conversion.
	 */
	public MyDialog(final MyParameters parameters) {
		/*
		 * Get a layout containing a single column and two rows, where the top
		 * row height equals 30.
		 */
		double size[][] = { {TableLayoutConstants.FILL, TableLayoutConstants.FILL}, 
				{TableLayoutConstants.FILL, TableLayoutConstants.FILL, TableLayoutConstants.FILL, TableLayoutConstants.FILL, TableLayoutConstants.FILL }};
		setLayout(new TableLayout(size));
		
		//////////// MINIMAL SET OF PLACES
		
		final JCheckBox minimalPlacesBox = SlickerFactory.instance().createCheckBox("Discover minimal set of places", false);
		
		minimalPlacesBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				parameters.setMinimalPlaces(minimalPlacesBox.isSelected());
			}

		});

		add(minimalPlacesBox, "1, 0");
		
		////////////// NORMALIZATION APPROACH
		Object normalization_approaches[] = new Object [3];
		normalization_approaches[0] = "minmax";
		normalization_approaches[1] = "log_transformation";
		normalization_approaches[2] = "winsorization";
	
		
		final JList normalizationList = new javax.swing.JList(normalization_approaches);

	
		normalizationList.setName("Select normalization approach");
		normalizationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Map<String, MyParameters.Normalization> normalizationMap = new HashMap<>();
        normalizationMap.put("minmax", MyParameters.Normalization.MINMAX);
        normalizationMap.put("log_transformation", MyParameters.Normalization.LOG);
        normalizationMap.put("winsorization", MyParameters.Normalization.WINSOR);

		
        normalizationList.addListSelectionListener(new ListSelectionListener() {
        	
        	
			public void valueChanged(ListSelectionEvent e) {
				MyParameters.Normalization selection = normalizationMap.get((String) normalizationList.getSelectedValue());
				parameters.setNormalizationApproach(selection);;
			}
		});
		JScrollPane classifierScrollPaneCoeff = new javax.swing.JScrollPane();
		SlickerDecorator.instance().decorate(classifierScrollPaneCoeff, SlickerColors.COLOR_BG_3, SlickerColors.COLOR_FG,
				SlickerColors.COLOR_BG_1);
		classifierScrollPaneCoeff.setPreferredSize(new Dimension(100, 100));
		classifierScrollPaneCoeff.setViewportView(normalizationList);
		add(classifierScrollPaneCoeff, "1, 1");
		
		////////////// SEQUENCE MATRIX
		Object approaches[] = new Object [6];
		approaches[0] = "DF matrix";
		approaches[1] = "EF matrix";
		approaches[2] = "EF weighted matrix";
		approaches[3] = "DDM matrix";
		approaches[4] = "EDM matrix";
		approaches[5] = "EDM weighted matrix";
		
		final JList approachList = new javax.swing.JList(approaches);

	
		approachList.setName("Select matrix that will act as sequence matrix");
		approachList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
        Map<String, MyParameters.SequenceMatrix> sequenceMatrixMap = new HashMap<>();
        sequenceMatrixMap.put("DF matrix", MyParameters.SequenceMatrix.DF);
        sequenceMatrixMap.put("EF matrix", MyParameters.SequenceMatrix.EF);
        sequenceMatrixMap.put("EF weighted matrix", MyParameters.SequenceMatrix.EFWEIGHTED);
        sequenceMatrixMap.put("DDM matrix", MyParameters.SequenceMatrix.DDM);
        sequenceMatrixMap.put("EDM matrix", MyParameters.SequenceMatrix.EDM);
        sequenceMatrixMap.put("EDM weighted matrix", MyParameters.SequenceMatrix.EDMWEIGHTED);

		
		approachList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				MyParameters.SequenceMatrix selection = sequenceMatrixMap.get((String) approachList.getSelectedValue());
				parameters.setSequenceMatrix(selection);
			}
		});
		JScrollPane classifierScrollPane = new javax.swing.JScrollPane();
		SlickerDecorator.instance().decorate(classifierScrollPane, SlickerColors.COLOR_BG_3, SlickerColors.COLOR_FG,
				SlickerColors.COLOR_BG_1);
		classifierScrollPane.setPreferredSize(new Dimension(100, 100));
		classifierScrollPane.setViewportView(approachList);
		add(classifierScrollPane, "1, 2");
		
		//////////////// CHOICE MATRIX
		Object approaches_choice[] = new Object [6];
		approaches_choice[0] = "DDM matrix";
		approaches_choice[1] = "EDM matrix";
		approaches_choice[2] = "EDM weighted matrix";

		
		final JList approachListChoice = new javax.swing.JList(approaches_choice);

		approachListChoice.setName("Select matrix to act as choice matrix");
		approachListChoice.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
        Map<String, MyParameters.ChoiceMatrix> choiceMatrixMap = new HashMap<>();
        choiceMatrixMap.put("DDM matrix", MyParameters.ChoiceMatrix.DDM);
        choiceMatrixMap.put("EDM matrix", MyParameters.ChoiceMatrix.EDM);
        choiceMatrixMap.put("EDM weighted matrix", MyParameters.ChoiceMatrix.EDMWEIGHTED);
		
		approachListChoice.addListSelectionListener(new ListSelectionListener() {
			MyParameters.ChoiceMatrix selection = choiceMatrixMap.get((String) approachListChoice.getSelectedValue());
			public void valueChanged(ListSelectionEvent e) {
				parameters.setChoiceMatrix(selection);
			}
		});
		JScrollPane classifierScrollPaneTwo = new javax.swing.JScrollPane();
		SlickerDecorator.instance().decorate(classifierScrollPaneTwo, SlickerColors.COLOR_BG_3, SlickerColors.COLOR_FG,
				SlickerColors.COLOR_BG_1);
		classifierScrollPaneTwo.setPreferredSize(new Dimension(100, 100));
		classifierScrollPaneTwo.setViewportView(approachListChoice);
		add(classifierScrollPaneTwo, "1, 3");

		//////////////// CONTEXT WINDOW SIZE

		final NiceSlider sliderWindow = SlickerFactory.instance()
				.createNiceIntegerSlider("Select context window size", 1,
						10, 1, Orientation.HORIZONTAL);
		

		ChangeListener listenerWindow = new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				parameters.setContextWindowSize(sliderWindow.getSlider().getValue());
			}
		};

		sliderWindow.addChangeListener(listenerWindow);
		listenerWindow.stateChanged(null);
		add(sliderWindow, "0, 0");
		
		///////////////// CHOICE PLACE NET COMPLEXITY
		
		final NiceSlider sliderDepth = SlickerFactory.instance()
				.createNiceIntegerSlider("Select complexity of choice places", 2,
						6, 2, Orientation.HORIZONTAL);
		

		ChangeListener listenerDepth = new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				parameters.setChoicePNComplexity(sliderDepth.getSlider().getValue());
			}
		};

		sliderDepth.addChangeListener(listenerDepth);
		listenerDepth.stateChanged(null);
		add(sliderDepth, "0, 1");
		

		//////////////// SEQUENCE THRESHOLD

		final NiceSlider sliderAlpha = SlickerFactory.instance()
				.createNiceIntegerSlider("Select sequence threshold (%)", 0, 
						100, 1, Orientation.HORIZONTAL);
		
		ChangeListener listenerAlpha = new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				parameters.setSequenceThreshold(sliderAlpha.getSlider().getValue() / 100.0);
			}
		};
		
		sliderAlpha.addChangeListener(listenerAlpha);
		listenerAlpha.stateChanged(null);
		add(sliderAlpha, "0, 2");
		
		/////////////// CHOICE THRESHOLD
		
		final NiceSlider sliderBeta = SlickerFactory.instance()
				.createNiceIntegerSlider("Select choise threshold (%)", 0, 
						100, 1, Orientation.HORIZONTAL);
		
		ChangeListener listenerBeta = new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				parameters.setChoiceThreshold(sliderBeta.getSlider().getValue() / 100.0);
			}
		};
		
		sliderBeta.addChangeListener(listenerBeta);
		listenerBeta.stateChanged(null);
		add(sliderBeta, "0, 3");

	}
}
