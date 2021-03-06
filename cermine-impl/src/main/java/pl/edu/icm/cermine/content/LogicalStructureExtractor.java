package pl.edu.icm.cermine.content;

import pl.edu.icm.cermine.content.model.BxDocContentStructure;
import pl.edu.icm.cermine.content.model.DocumentContentStructure;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.structure.model.*;
import pl.edu.icm.cermine.tools.classification.features.FeatureVectorBuilder;
import pl.edu.icm.cermine.tools.classification.knn.KnnModel;

/**
 *
 * @author Dominika Tkaczyk
 */
public class LogicalStructureExtractor {

    public DocumentContentStructure extractStructure(KnnModel<BxZoneLabel> junkFilterModel, 
            KnnModel<BxZoneLabel> headerModel,
            FeatureVectorBuilder<BxZone, BxPage> junkVectorBuilder, 
            FeatureVectorBuilder<BxLine, BxPage> classVectorBuilder, 
            FeatureVectorBuilder<BxLine, BxPage> clustVectorBuilder, 
            BxDocument document) throws AnalysisException {
        ContentJunkFilter junkFilter = new ContentJunkFilter();
        document = junkFilter.filterJunk(junkFilterModel, junkVectorBuilder, document);
        
        ContentHeaderExtractor headerExtractor = new ContentHeaderExtractor(); 
        BxDocContentStructure tmpContentStructure = headerExtractor.extractHeaders(headerModel, classVectorBuilder, 
                clustVectorBuilder, document);

        ContentCleaner contentCleaner = new ContentCleaner();
        contentCleaner.cleanupContent(tmpContentStructure);
                
        DocumentContentStructure contentStructure = new DocumentContentStructure();
        contentStructure.build(tmpContentStructure);
        
        return contentStructure;
    }
    
}
