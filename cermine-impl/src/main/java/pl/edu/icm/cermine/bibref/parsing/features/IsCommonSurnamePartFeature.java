package pl.edu.icm.cermine.bibref.parsing.features;

import java.util.List;
import pl.edu.icm.cermine.bibref.parsing.model.Citation;
import pl.edu.icm.cermine.bibref.parsing.model.CitationToken;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class IsCommonSurnamePartFeature implements FeatureCalculator<CitationToken, Citation> {

    private static String featureName = "IsCommonSurnamePart";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(CitationToken object, Citation context) {
        String text = object.getText();
        if (text.equalsIgnoreCase("van") || text.equalsIgnoreCase("de") || text.equalsIgnoreCase("der")) {
            return 1;
        }
        if (text.matches("^Mc[A-Z].*$")) {
            return 1;
        }

        List<CitationToken> tokens = context.getTokens();
        int index = tokens.indexOf(object);
        if (index + 2 < tokens.size() && text.equals("O") && tokens.get(index + 1).getText().equals("'")
                && tokens.get(index + 2).getText().matches("^[A-Z].*$")) {
            return 1;
        }
        if (index + 1 < tokens.size() && index - 1 >= 0 && text.equals("'")
                && tokens.get(index - 1).getText().equals("O")
                && tokens.get(index + 1).getText().matches("^[A-Z].*$")) {
            return 1;
        }
        return 0;
    }

}
