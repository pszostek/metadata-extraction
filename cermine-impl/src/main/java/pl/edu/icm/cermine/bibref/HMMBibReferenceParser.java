package pl.edu.icm.cermine.bibref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pl.edu.icm.cermine.bibref.model.BibEntry;
import pl.edu.icm.cermine.bibref.parsing.model.Citation;
import pl.edu.icm.cermine.bibref.parsing.model.CitationToken;
import pl.edu.icm.cermine.bibref.parsing.model.CitationTokenLabel;
import pl.edu.icm.cermine.bibref.parsing.tools.CitationUtils;
import pl.edu.icm.cermine.tools.classification.features.FeatureVector;
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder;
import pl.edu.icm.cermine.tools.classification.hmm.HMMService;
import pl.edu.icm.cermine.tools.classification.hmm.HMMStorage;
import pl.edu.icm.cermine.tools.classification.hmm.model.HMMProbabilityInfo;

/**
 * Hidden Markov Models-based citation parser.
 * 
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class HMMBibReferenceParser implements BibReferenceParser<BibEntry> {

	private HMMService hmmService;
	private HMMProbabilityInfo<CitationTokenLabel> labelProbabilities;
	private FeatureVectorBuilder<CitationToken, Citation> featureVectorBuilder;

	public HMMBibReferenceParser(HMMService hmmService, HMMStorage hmmStorage,
			String hmmId,
			FeatureVectorBuilder<CitationToken, Citation> featureVectorBuilder)
			throws IOException {
		this.hmmService = hmmService;
		this.featureVectorBuilder = featureVectorBuilder;
		this.labelProbabilities = hmmStorage.getProbabilityInfo(hmmId);
	}

	public HMMBibReferenceParser(HMMService hmmService,
			HMMProbabilityInfo<CitationTokenLabel> labelProbabilities,
			FeatureVectorBuilder<CitationToken, Citation> featureVectorBuilder) {
		this.hmmService = hmmService;
		this.labelProbabilities = labelProbabilities;
		this.featureVectorBuilder = featureVectorBuilder;
	}

	@Override
	public BibEntry parseBibReference(String text) {
		Citation citation = CitationUtils.stringToCitation(text);
        List<CitationToken> tokens = citation.getTokens();

		List<FeatureVector> featureVectors = new ArrayList<FeatureVector>();
		for (CitationToken token : tokens) {
			featureVectors.add(featureVectorBuilder.getFeatureVector(token,
					citation));
		}

		List<CitationTokenLabel> labels = hmmService.viterbiMostProbableStates(
				labelProbabilities, Arrays.asList(CitationTokenLabel.values()),
				featureVectors);
		for (int i = 0; i < tokens.size(); i++) {
			tokens.get(i).setLabel(labels.get(i));
		}

		CitationUtils.removeHMMLabels(citation);
		return CitationUtils.citationToBibref(citation);
	}

	public void setFeatureVectorBuilder(
			FeatureVectorBuilder<CitationToken, Citation> featureVectorBuilder) {
		this.featureVectorBuilder = featureVectorBuilder;
	}

	public void setHmmService(HMMService hmmService) {
		this.hmmService = hmmService;
	}

	public void setLabelProbabilities(
			HMMProbabilityInfo<CitationTokenLabel> labelProbabilities) {
		this.labelProbabilities = labelProbabilities;
	}

}
