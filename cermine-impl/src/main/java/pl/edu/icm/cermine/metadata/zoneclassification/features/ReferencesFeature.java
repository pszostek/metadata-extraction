package pl.edu.icm.cermine.metadata.zoneclassification.features;

import java.util.regex.Pattern;
import pl.edu.icm.cermine.structure.model.BxLine;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (d.tkaczyk@icm.edu.pl)
 */
public class ReferencesFeature implements FeatureCalculator<BxZone, BxPage> {

    private static String featureName = "References";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(BxZone zone, BxPage page) {
        int refDigits = 0;
        int refIndents = 0;
        for (BxLine line : zone.getLines()) {
            if (Pattern.matches("^\\[\\d+\\].*", line.toText()) || Pattern.matches("^\\d+\\..*", line.toText())) {
                refDigits++;
            }
            if (zone.getBounds().getX() + 8 < line.getBounds().getX()) {
                refIndents++;
            }
        }
        return ((double)refDigits > (double)zone.getLines().size() / 4.0
                || (double)refIndents > (double)zone.getLines().size() / 4.0) ? 1 : 0;
    }

}
