package pl.edu.icm.cermine.content.features.line;

import pl.edu.icm.cermine.structure.model.BxLine;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class IndentationFeature implements FeatureCalculator<BxLine, BxPage> {

    private static String featureName = "Indentation";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(BxLine line, BxPage page) {
        int i = 0;
        BxLine l = line;
        double meanX = 0;
        while (l.hasNext() && i < 5) {
            l = l.getNext();
            if ((line.getX() < l.getX() && l.getX() < line.getX() + line.getWidth()) 
                    || (l.getX() < line.getX() && line.getX() < l.getX() + l.getWidth())) {
                meanX += l.getX();
                i++;
            } else {
                break;
            }
        }
        if (i == 0) {
            return 0.0;
        }
        
        meanX /= i;
        return Math.abs(line.getX() - meanX) / (double) line.getWidth();
    }
    
}
