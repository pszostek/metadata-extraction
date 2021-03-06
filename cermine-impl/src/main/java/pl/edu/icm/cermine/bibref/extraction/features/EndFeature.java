package pl.edu.icm.cermine.bibref.extraction.features;

import pl.edu.icm.cermine.bibref.extraction.model.BxDocumentBibReferences;
import pl.edu.icm.cermine.structure.model.BxLine;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class EndFeature implements FeatureCalculator<BxLine, BxDocumentBibReferences> {

    private static String featureName = "End";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(BxLine refLine, BxDocumentBibReferences refs) {
        return refs.getZone(refLine).getBounds().getX() + refs.getZone(refLine).getBounds().getWidth()
                - refLine.getBounds().getX() - refLine.getBounds().getWidth();
    }
    
}
