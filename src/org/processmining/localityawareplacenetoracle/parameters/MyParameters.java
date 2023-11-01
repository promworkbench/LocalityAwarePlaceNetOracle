package org.processmining.localityawareplacenetoracle.parameters;

/**
 * Parameters configuration for a process mining plugin.
 */
public class MyParameters {
    public static final int DEFAULT_CONTEXT_WINDOW_SIZE = 1;
    public static final int DEFAULT_CHOICE_PN_COMPLEXITY = 3;
    public static final double DEFAULT_SEQUENCE_THRESHOLD = 0.01;
    public static final double DEFAULT_CHOICE_THRESHOLD = 0.01;
    public static final boolean DEFAULT_MINIMAL_PLACES = false;
    public static final SequenceMatrix DEFAULT_SEQUENCE_MATRIX = SequenceMatrix.EF;
    public static final ChoiceMatrix DEFAULT_CHOICE_MATRIX = ChoiceMatrix.EDM;
    public static final Normalization DEFAULT_NORMALIZATION_APPROACH = Normalization.MINMAX;

    private int contextWindowSize;
    private int choicePNComplexity;
    private double sequenceThreshold;
    private double choiceThreshold;
    private boolean minimalPlaces;
    private SequenceMatrix sequenceMatrix;
    private ChoiceMatrix choiceMatrix;
    private Normalization normalizationApproach;

    public enum SequenceMatrix {
        DF, EF, DDM, EDM, EFWEIGHTED, EDMWEIGHTED
    }

    public enum ChoiceMatrix {
        DDM, EDM, EDMWEIGHTED
    }

    public enum Normalization {
        MINMAX, LOG, WINSOR
    }

    /**
     * Initializes parameters with default values.
     */
    public MyParameters() {
        this.contextWindowSize = DEFAULT_CONTEXT_WINDOW_SIZE;
        this.choicePNComplexity = DEFAULT_CHOICE_PN_COMPLEXITY;
        this.sequenceThreshold = DEFAULT_SEQUENCE_THRESHOLD;
        this.choiceThreshold = DEFAULT_CHOICE_THRESHOLD;
        this.minimalPlaces = DEFAULT_MINIMAL_PLACES;
        this.sequenceMatrix = DEFAULT_SEQUENCE_MATRIX;
        this.choiceMatrix = DEFAULT_CHOICE_MATRIX;
        this.normalizationApproach = DEFAULT_NORMALIZATION_APPROACH;
    }

    public int getContextWindowSize() {
        return contextWindowSize;
    }

    public void setContextWindowSize(int contextWindowSize) {
        if (contextWindowSize <= 0) {
            throw new IllegalArgumentException("Context window size must be positive.");
        }
        this.contextWindowSize = contextWindowSize;
    }

    public int getChoicePNComplexity() {
        return choicePNComplexity;
    }

    public void setChoicePNComplexity(int choicePNComplexity) {
        if (choicePNComplexity < 2) {
            throw new IllegalArgumentException("Choice PN complexity must be at least 2.");
        }
        this.choicePNComplexity = choicePNComplexity;
    }

    public double getSequenceThreshold() {
        return sequenceThreshold;
    }

    public void setSequenceThreshold(double sequenceThreshold) {
        if (sequenceThreshold < 0 || sequenceThreshold > 1) {
            throw new IllegalArgumentException("Sequence threshold must be between 0 and 1.");
        }
        this.sequenceThreshold = sequenceThreshold;
    }

    public double getChoiceThreshold() {
        return choiceThreshold;
    }

    public void setChoiceThreshold(double choiceThreshold) {
        if (choiceThreshold < 0 || choiceThreshold > 1) {
            throw new IllegalArgumentException("Choice threshold must be between 0 and 1.");
        }
        this.choiceThreshold = choiceThreshold;
    }

    public boolean isMinimalPlaces() {
        return minimalPlaces;
    }

    public void setMinimalPlaces(boolean minimalPlaces) {
        this.minimalPlaces = minimalPlaces;
    }

    public SequenceMatrix getSequenceMatrix() {
        return sequenceMatrix;
    }

    public void setSequenceMatrix(SequenceMatrix sequenceMatrix) {
        this.sequenceMatrix = sequenceMatrix;
    }

    public ChoiceMatrix getChoiceMatrix() {
        return choiceMatrix;
    }

    public void setChoiceMatrix(ChoiceMatrix choiceMatrix) {
        this.choiceMatrix = choiceMatrix;
    }

    public Normalization getNormalizationApproach() {
        return normalizationApproach;
    }

    public void setNormalizationApproach(Normalization normalizationApproach) {
        this.normalizationApproach = normalizationApproach;
    }


}