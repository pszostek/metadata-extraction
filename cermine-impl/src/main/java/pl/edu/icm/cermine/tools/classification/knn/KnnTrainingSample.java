package pl.edu.icm.cermine.tools.classification.knn;

import pl.edu.icm.cermine.tools.classification.features.FeatureVector;

/**
 *
 * @author Dominika Tkaczyk
 */
public class KnnTrainingSample<T> {
    
    private FeatureVector features;
    private T label;
  
    public KnnTrainingSample(FeatureVector features, T label) {
        this.features = features;
        this.label = label;
    }
    
    public FeatureVector getFeatures() {
        return features;
    }

    public void setFeatures(FeatureVector features) {
        this.features = features;
    }

    public T getLabel() {
        return label;
    }

    public void setLabel(T label) {
        this.label = label;
    }

}
