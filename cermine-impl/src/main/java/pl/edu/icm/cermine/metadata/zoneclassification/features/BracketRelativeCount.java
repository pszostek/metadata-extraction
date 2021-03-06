package pl.edu.icm.cermine.metadata.zoneclassification.features;

import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

public class BracketRelativeCount implements FeatureCalculator<BxZone, BxPage> {

	private static String featureName = "BracketRelativeCount";
	
	@Override
	public String getFeatureName() {
		return featureName;
	}

	@Override
	public double calculateFeatureValue(BxZone zone, BxPage page) {
		double brackets = new BracketCountFeature().calculateFeatureValue(zone, page);
		double chars = new CharCountFeature().calculateFeatureValue(zone, page);
		return brackets/chars; 
	}
}
