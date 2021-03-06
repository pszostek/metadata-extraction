package pl.edu.icm.cermine.tools.classification.general;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder;

/**
 * Simple feature vector builder.
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class SimpleFeatureVectorBuilder<S, T> implements FeatureVectorBuilder<S, T> {

    Map<String, FeatureCalculator<S, T>> featureCalculators = new HashMap<String, FeatureCalculator<S, T>>();

    @Override
    public void setFeatureCalculators(Collection<FeatureCalculator<S, T>> featureCalculators) {
        for (FeatureCalculator<S, T> featureCalculator : featureCalculators) {
            this.featureCalculators.put(featureCalculator.getFeatureName(), featureCalculator);
        }
    }

    @Override
    public SimpleFeatureVector getFeatureVector(S object, T context) {
        SimpleFeatureVector featureVector = new SimpleFeatureVector();
        for (String name : featureCalculators.keySet()) {
            featureVector.addFeature(name, featureCalculators.get(name).calculateFeatureValue(object, context));
        }
        return featureVector;
    }

    @Override
    public Set<String> getFeatureNames() {
        return featureCalculators.keySet();
    }
    
    @Override
    public int size() {
    	return featureCalculators.size();
    }

}
