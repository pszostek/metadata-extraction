package pl.edu.icm.cermine.metadata.zoneclassification.features;

import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/** 
 * @author Pawel Szostek (p.szostek@icm.edu.pl) 
 */

public class DistanceFromNearestNeighbourFeature implements
		FeatureCalculator<BxZone, BxPage> {
	private static String featureName = "DistanceFromNearestNeighbour";

	@Override
	public String getFeatureName() {
		return featureName;
	}

	private static Double euclideanDist(Double x0, Double y0, Double x1, Double y1) {
		return Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
	}

	@Override
	public double calculateFeatureValue(BxZone zone, BxPage page) {
		Double minDist = Double.MAX_VALUE;
		
		for (BxZone otherZone : page.getZones()) {
			if (otherZone == zone)
				continue;

			Double dist;
			Double cx, cy, cw, ch, ox, oy, ow, oh;
			cx = zone.getBounds().getX();
			cy = zone.getBounds().getY();
			cw = zone.getBounds().getWidth();
			ch = zone.getBounds().getHeight();

			ox = otherZone.getBounds().getX();
			oy = otherZone.getBounds().getY();
			ow = otherZone.getBounds().getWidth();
			oh = otherZone.getBounds().getHeight();

			// Determine Octant
			//
			// 0 | 1 | 2
			// __|___|__
			// 7 | 9 | 3
			// __|___|__
			// 6 | 5 | 4

			Integer oct;
			if (cx + cw <= ox) {
				if (cy + ch <= oy)
					oct = 4; // 0;
				else if (cy >= oy + oh)
					oct = 2; // 6;
				else
					oct = 3; // 7;
			} else if (ox + ow <= cx) {
				if (cy + ch <= oy)
					oct = 6;
				else if (oy + oh <= cy)
					oct = 0;
				else
					oct = 7;
			} else if (cy + ch <= oy) {
				oct = 5;
			} else if(oy + oh <= cy) {
				oct = 1;
			} else { //two zones
				continue;
			}
			// determine distance based on octant
			switch (oct) {
			case 0:
				dist = euclideanDist(ox + ow, oy + oh, cx, cy);
				break;
			case 1:
				dist = cy - (oy + oh);
				break;
			case 2:
				dist = euclideanDist(ox, oy + oh, cx + cw, cy);
				break;
			case 3:
				dist = ox - (cx + cw);
				break;
			case 4:
				dist = euclideanDist(cx + cw, cy + ch, ox, oy);
				break;
			case 5:
				dist = oy - (cy + ch);
				break;
			case 6:
				dist = euclideanDist(ox + ow, oy, cx, cx + ch);
				break;
			case 7:
				dist = cx - (ox + ow);
				break;
			default:
				dist = Double.MAX_VALUE;
			}
			//minDist = Math.min(minDist, dist);
			if(dist < minDist) {
				minDist = dist;
			}
		}
		if(minDist == Double.MAX_VALUE)
			return 0.0;
		else
			return minDist;
	}
};
