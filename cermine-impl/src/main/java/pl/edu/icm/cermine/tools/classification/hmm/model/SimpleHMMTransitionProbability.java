package pl.edu.icm.cermine.tools.classification.hmm.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.edu.icm.cermine.structure.tools.ProbabilityDistribution;
import pl.edu.icm.cermine.tools.classification.hmm.training.TrainingElement;

/**
 * Simple Hidden Markov Model transition probability implementation.
 *
 * @author Dominika Tkaczyk (d.tkaczyk@icm.edu.pl)
 */
public class SimpleHMMTransitionProbability<S> implements HMMTransitionProbability<S> {

    private Map<S, ProbabilityDistribution<S>> probability;

    private double zeroProbabilityValue;

    public SimpleHMMTransitionProbability(List<TrainingElement<S>> trainingElements) {
        this(trainingElements, 0.0);
    }

    public SimpleHMMTransitionProbability(List<TrainingElement<S>> trainingElements, double zeroProbabilityValue) {
        this.zeroProbabilityValue = zeroProbabilityValue;
        probability = new HashMap<S, ProbabilityDistribution<S>>();
        for (TrainingElement<S> element : trainingElements) {
            if (element.getNextLabel() != null) {
                if (!probability.containsKey(element.getLabel())) {
                    probability.put(element.getLabel(), new ProbabilityDistribution<S>());
                }
                probability.get(element.getLabel()).addEvent(element.getNextLabel());
            }
        }
    }

    @Override
    public double getProbability(S startLabel, S endLabel) {
        double prob = zeroProbabilityValue;
        if (probability.containsKey(startLabel)) {
            prob = probability.get(startLabel).getProbability(endLabel);
            if (prob == 0) {
                prob = zeroProbabilityValue;
            }
        }
        return prob;
    }

}
