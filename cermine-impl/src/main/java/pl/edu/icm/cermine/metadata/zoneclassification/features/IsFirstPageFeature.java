package pl.edu.icm.cermine.metadata.zoneclassification.features;

import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/** 
 * @author Pawel Szostek (p.szostek@icm.edu.pl) 
 */

public class IsFirstPageFeature implements FeatureCalculator<BxZone, BxPage> {
	private static String featureName = "IsFirstPage";

	@Override
	public String getFeatureName() {
		return featureName;
	}

	@Override
	public double calculateFeatureValue(BxZone zone, BxPage page) {
		return page.getId().equals(new String("0")) == true ? 1.0 : 0.0;
	}
}
