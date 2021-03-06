package pl.edu.icm.cermine.metadata.zoneclassification.features;

import pl.edu.icm.cermine.bibref.parsing.features.AbstractFeatureCalculator;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;

public class IsRightFeature extends AbstractFeatureCalculator<BxZone, BxPage> {

	private static double TRESHOLD = 0.3;
	@Override
	public double calculateFeatureValue(BxZone object, BxPage context) {
		if(object.getX()+object.getWidth()/2.0 > context.getWidth()*(1-TRESHOLD))
			return 1.0;
		else
			return 0.0;
	}

}
