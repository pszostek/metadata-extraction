package pl.edu.icm.cermine.metadata.zoneclassification.features;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;
public class IsLowestOnThePageFeature implements FeatureCalculator<BxZone, BxPage>{
    private static String featureName = "IsLowestOnThePage";

	@Override
	public String getFeatureName() {
		return featureName;
	}
	
	private static class yCoordinateComparator implements Comparator<BxZone> {
		@Override
		public int compare(BxZone z1, BxZone z2) {
			return (z1.getY() + z1.getHeight() < z2.getY() + z2.getHeight() ? -1
					: (z1.getY() + z1.getHeight() == z2.getY() + z2.getHeight() ? 0
							: 1));
		}
	}
	
	@Override
	public double calculateFeatureValue(BxZone zone, BxPage page) {	
		List<BxZone> zones = new ArrayList<BxZone>(page.getZones());
		Collections.sort(zones, new yCoordinateComparator());
		BxZone lastZone = zones.get(zones.size()-1);
		final double IDENT_TRESHOLD = 10.0;
		if(zone == lastZone)
			return 1.0;
		else
			if(Math.abs(lastZone.getY()+lastZone.getHeight() - zone.getY()+zone.getHeight()) <= IDENT_TRESHOLD) 
				return 1.0;
			else
				return 0.0;
	}
}
