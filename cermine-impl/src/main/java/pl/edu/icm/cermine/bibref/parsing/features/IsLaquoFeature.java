package pl.edu.icm.cermine.bibref.parsing.features;

import pl.edu.icm.cermine.bibref.parsing.model.Citation;
import pl.edu.icm.cermine.bibref.parsing.model.CitationToken;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class IsLaquoFeature implements FeatureCalculator<CitationToken, Citation> {

    private static String featureName = "IsLaquo";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(CitationToken object, Citation context) {
        String text = object.getText();
        return (text.length() == 1 && text.charAt(0) == '\u00AB') ? 1 : 0;
    }

}
