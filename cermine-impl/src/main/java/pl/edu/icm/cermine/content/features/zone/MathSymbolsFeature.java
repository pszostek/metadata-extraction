package pl.edu.icm.cermine.content.features.zone;

import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class MathSymbolsFeature implements FeatureCalculator<BxZone, BxPage> {

    private static String featureName = "MathSymbols";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(BxZone zone, BxPage page) {
        return (zone.toText().matches("^.*[=\\u2200-\\u22FF].*$")) ? 1 : 0;
    }
    
}
