package pl.edu.icm.cermine.content.features.line;

import pl.edu.icm.cermine.structure.model.BxLine;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.tools.classification.features.FeatureCalculator;

/**
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class WordsAllUppercaseFeature implements FeatureCalculator<BxLine, BxPage> {

    private static String featureName = "WordsAllUppercase";

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public double calculateFeatureValue(BxLine line, BxPage page) {
        String text = line.toText();
        String[] words = text.split("\\s");
        int upperWordsCount = 0;
        for (String word : words) {
            if (word.matches("^[A-Z][-'A-Z]+$")) {
                upperWordsCount++;
            }
        }
        return (double) upperWordsCount / (double) words.length;
    }
    
}
