package pl.edu.icm.cermine.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import pl.edu.icm.cermine.metadata.zoneclassification.tools.BxDocsToHMMConverter;
import pl.edu.icm.cermine.structure.model.BxDocument;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.structure.model.BxZone;
import pl.edu.icm.cermine.structure.model.BxZoneLabel;
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder;
import pl.edu.icm.cermine.tools.classification.general.DirExtractor;
import pl.edu.icm.cermine.tools.classification.general.DocumentsExtractor;
import pl.edu.icm.cermine.tools.classification.hmm.training.TrainingElement;

public class EvaluationUtils {
    public static List<BxDocument> getDocumentsFromPath(String inputDirPath)
	{
		if (inputDirPath == null) {
			throw new NullPointerException("Input directory must not be null.");
		}

		if (!inputDirPath.endsWith(File.separator)) {
			inputDirPath += File.separator;
		}
		DocumentsExtractor extractor = new DirExtractor(inputDirPath);
		
		List<BxDocument> evaluationDocuments;
		try {
			 evaluationDocuments = extractor.getDocuments();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to get evaluation documents from the indicated location! Got exception: " + e);
		}
		return evaluationDocuments;
	}
    
	public static List<TrainingElement<BxZoneLabel>> getTrainingElements(BxDocument trainingDocument, FeatureVectorBuilder<BxZone, BxPage> featureVectorBuilder) {
		List<BxDocument> dummyList = new ArrayList<BxDocument>(1);
		dummyList.add(trainingDocument);
		return getTrainingElements(new ArrayList<BxDocument>(dummyList), featureVectorBuilder);
	}
	
	public static List<TrainingElement<BxZoneLabel>> getTrainingElements(List<BxDocument> trainingDocuments, FeatureVectorBuilder<BxZone, BxPage> featureVectorBuilder) { 
		BxDocsToHMMConverter node = new BxDocsToHMMConverter();
		node.setLabelMap(null);

		node.setFeatureVectorBuilder(featureVectorBuilder);
		List<TrainingElement<BxZoneLabel>> trainingElements = null;
		try {
			trainingElements = node.process(trainingDocuments);
		} catch (Exception e) {
			System.out.println(e.getCause());
			e.printStackTrace();
			System.exit(1);
		}
		return trainingElements;
	}
}
