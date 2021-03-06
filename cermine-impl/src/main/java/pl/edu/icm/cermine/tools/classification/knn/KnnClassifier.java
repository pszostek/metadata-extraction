package pl.edu.icm.cermine.tools.classification.knn;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import pl.edu.icm.cermine.tools.classification.features.FeatureVector;
import pl.edu.icm.cermine.tools.classification.metrics.FeatureVectorDistanceMetric;

/**
 *
 * @author Dominika Tkaczyk
 * 
 */
public class KnnClassifier<T> {
    
    public T classify(KnnModel<T> model, FeatureVectorDistanceMetric metric, FeatureVector sample, int samplesCount) {
       
        FVEuclideanDistanceComparator comparator = new FVEuclideanDistanceComparator(sample, metric);
        
        KnnTrainingSample[] voters = new KnnTrainingSample[samplesCount];
        Iterator<KnnTrainingSample<T>> trainingIterator = model.getIterator();
        int i = 0;
        KnnTrainingSample<T> largest = null;
        int largestIndex = 0;
        while (trainingIterator.hasNext()) {
            if (i < samplesCount) {
                voters[i] = trainingIterator.next();
            } else {
                if (largest == null) {
                    largest = voters[0];
                    largestIndex = 0;
                    for (int j = 1; j < samplesCount; j++) {
                        if (comparator.compare(largest, voters[j]) < 0) {
                            largest = voters[j];
                            largestIndex = j;
                        }
                    }
                }
                KnnTrainingSample<T> next = trainingIterator.next();
                if (comparator.compare(largest, next) > 0) {
                    voters[largestIndex] = next;
                    largest = null;
                }
            }
            i++;
        }
        
        Map<T,Integer> labelCountMap = new HashMap<T,Integer>();
        for (KnnTrainingSample<T> trainingSample : voters) {
            if (trainingSample != null) {
                T label = trainingSample.getLabel();
                if (labelCountMap.get(label) == null) {
                    labelCountMap.put(label, 1);
                } else {
                    labelCountMap.put(label, labelCountMap.get(label)+1);
                }
            }
        }
        
        T label = null;
        int labelCount = 0;
        for (T l : labelCountMap.keySet()) {
            if (labelCountMap.get(l) > labelCount) {
                label = l;
                labelCount = labelCountMap.get(l);
            }
        }
       
        return label;
    }

    public class FVEuclideanDistanceComparator implements Comparator<KnnTrainingSample<T>> {
        
        public FeatureVectorDistanceMetric metric;
        private FeatureVector sample;

        public FVEuclideanDistanceComparator(FeatureVector sample, FeatureVectorDistanceMetric metric) {
            this.sample = sample;
            this.metric = metric;
        }
        
        @Override
        public int compare(KnnTrainingSample<T> ts1, KnnTrainingSample<T> ts2) {
            double dist1 = metric.getDistance(sample, ts1.getFeatures());
            double dist2 = metric.getDistance(sample, ts2.getFeatures());
            if (dist1 < dist2) {
                return -1;
            } else if (dist1 > dist2) {
                return 1;
            } else {
                return 0;
            }
        }
 
    }
    
}
