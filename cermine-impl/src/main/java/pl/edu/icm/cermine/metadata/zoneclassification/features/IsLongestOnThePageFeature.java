package pl.edu.icm.cermine.metadata.zoneclassification.features;

import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

public class IsLongestOnThePageFeature implements FeatureCalculator<BxZone, BxPage> {
	private String featureName = "IsLongestOnThePage";
	@Override
	public String getFeatureName() {
		return featureName;
	}

	@Override
	public double calculateFeatureValue(BxZone object, BxPage context) {
		for(BxZone zone: context.getZones())
			if(zone.toText().length() > object.toText().length())
				return 0.0;
		return 1.0;
	}

}
