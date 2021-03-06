package pl.edu.icm.cermine.bibref.parsing.tools;

import java.io.IOException;
import java.util.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import pl.edu.icm.cermine.bibref.parsing.model.Citation;
import pl.edu.icm.cermine.bibref.parsing.model.CitationToken;
import pl.edu.icm.cermine.bibref.parsing.model.CitationTokenLabel;

/**
 * Citation extractor from NLM xml-s.
 *
 * @author Dominika Tkaczyk (dtkaczyk@icm.edu.pl)
 */
public class NlmCitationExtractor {

    public static String TAG_CITATION = "mixed-citation";

    public static String KEY_TEXT = "text";

    public static List<String> EXP_TAGS = Arrays.asList(
            "string-name", "italic", "bold", "sup", "sub", "styled-content", "monospace", "sans-serif", "underline",
            "xref", "inline-formula");

    private static final Map<String, CitationTokenLabel> TAGS_LABEL_MAP = new HashMap<String, CitationTokenLabel>();

    static {
        TAGS_LABEL_MAP.put("article-title",   CitationTokenLabel.ARTICLE_TITLE);
        TAGS_LABEL_MAP.put("conf-name",       CitationTokenLabel.CONF);
        TAGS_LABEL_MAP.put("named-content",   CitationTokenLabel.CONTENT);
        TAGS_LABEL_MAP.put("edition",         CitationTokenLabel.EDITION);
        TAGS_LABEL_MAP.put("given-names",     CitationTokenLabel.GIVENNAME);
        TAGS_LABEL_MAP.put("issue",           CitationTokenLabel.ISSUE);
        TAGS_LABEL_MAP.put("fpage",           CitationTokenLabel.PAGEF);
        TAGS_LABEL_MAP.put("lpage",           CitationTokenLabel.PAGEL);
        TAGS_LABEL_MAP.put("publisher-loc",   CitationTokenLabel.PUBLISHER_LOC);
        TAGS_LABEL_MAP.put("publisher-name",  CitationTokenLabel.PUBLISHER_NAME);
        TAGS_LABEL_MAP.put("sc",              CitationTokenLabel.SC);
        TAGS_LABEL_MAP.put("series",          CitationTokenLabel.SERIES);
        TAGS_LABEL_MAP.put("source",          CitationTokenLabel.SOURCE);
        TAGS_LABEL_MAP.put("surname",         CitationTokenLabel.SURNAME);
        TAGS_LABEL_MAP.put("text",            CitationTokenLabel.TEXT);
        TAGS_LABEL_MAP.put("uri",             CitationTokenLabel.URI);
        TAGS_LABEL_MAP.put("volume",          CitationTokenLabel.VOLUME);
        TAGS_LABEL_MAP.put("volume-series",   CitationTokenLabel.VOLUME_SERIES);
        TAGS_LABEL_MAP.put("year",            CitationTokenLabel.YEAR);
    }

    public static Set<Citation> extractCitations(InputSource source) throws JDOMException, IOException {
        Document doc = new SAXBuilder("org.apache.xerces.parsers.SAXParser").build(source);

        Iterator mixedCitations = doc.getDescendants(new Filter() {

            @Override
            public boolean matches(Object object) {
                return object instanceof Element && ((Element) object).getName().equals(TAG_CITATION);
            }
        });

        Set<Citation> citationSet = new HashSet<Citation>();

        while (mixedCitations.hasNext()) {
            Citation citation = new Citation();
            readElement((Element) mixedCitations.next(), citation);
            citationSet.add(citation);
        }
        return citationSet;
    }

    private static void readElement(Element element, Citation citation) {
        for (Object content : element.getContent()) {
            if (content instanceof Text) {
                String contentText = ((Text) content).getText();
                if (!contentText.matches("^[\\s]*$")) {
                    for (CitationToken token : CitationUtils.stringToCitation(contentText).getTokens()) {
                        token.setStartIndex(token.getStartIndex() + citation.getText().length());
                        token.setEndIndex(token.getEndIndex() + citation.getText().length());
                        token.setLabel(TAGS_LABEL_MAP.get(KEY_TEXT));
                        citation.addToken(token);
                    }
                    citation.appendText(contentText);
                } else {
                    citation.appendText(" ");
                }
            } else if (content instanceof Element) {
                Element contentElement = (Element) content;
                String contentElementName = contentElement.getName();
                if (TAGS_LABEL_MAP.containsKey(contentElementName)) {
                    for (CitationToken token : CitationUtils.stringToCitation(contentElement.getValue()).getTokens()) {
                        token.setStartIndex(token.getStartIndex() + citation.getText().length());
                        token.setEndIndex(token.getEndIndex() + citation.getText().length());
                        token.setLabel(TAGS_LABEL_MAP.get(contentElementName));
                        citation.addToken(token);
                    }
                    citation.appendText(contentElement.getValue());
                } else if (EXP_TAGS.contains(contentElementName)) {
                    readElement(contentElement, citation);
                }
            }
        }
    }
    
}
