package pl.edu.icm.cermine.metadata.zoneclassification.features;

import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/** 
 * @author Pawel Szostek (p.szostek@icm.edu.pl) 
 */

public class StartsWithDigitFeature implements
		FeatureCalculator<BxZone, BxPage> {

    private static String featureName = "StartsWithDigit";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(BxZone zone, BxPage page) {
    	if(zone.toText().length() == 0) {
    		return 0.0;
    	} else {
    		return (Character.isDigit(zone.toText().charAt(0))) ? 1.0 : 0.0;
    	}
    }

}
