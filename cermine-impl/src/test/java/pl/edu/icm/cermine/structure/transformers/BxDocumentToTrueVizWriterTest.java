package pl.edu.icm.cermine.structure.transformers;

import java.io.StringReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import pl.edu.icm.cermine.structure.model.*;

/**
 *
 * @author krusek
 */
public class BxDocumentToTrueVizWriterTest {

    private XPath xpath;
    private BxDocument bxDoc;
    private Document domDoc;

    @Before
    public void initialize() throws Exception {
        xpath = XPathFactory.newInstance().newXPath();
        bxDoc = getBxDocument();
        domDoc = bxToDom(bxDoc);
    }

    private void assertXpath(String expr, String expected) throws XPathExpressionException {
        String actual = (String) xpath.evaluate(expr, domDoc, XPathConstants.STRING);
        assertEquals(expected, actual);
    }

    private void assertXpathNoCase(String expr, String expected) throws XPathExpressionException {
        String actual = (String) xpath.evaluate(expr, domDoc, XPathConstants.STRING);
        assertEquals(expected.toLowerCase(), actual.toLowerCase());
    }

    private void assertXpath(String expr, double expected) throws XPathExpressionException {
        Double actual = (Double) xpath.evaluate(expr, domDoc, XPathConstants.NUMBER);
        assertEquals(expected, actual.doubleValue(), 0.00001);
    }

    private BxDocument getBxDocument() {
        BxWord w = new BxWord();
        w.setBounds(new BxBounds(0, 0, 1, 1));
        w.addChunks(new BxChunk(new BxBounds(0, 0, 1, 1), "a"));
        w.addChunks(new BxChunk(new BxBounds(0, 0, 1, 1), "b"));

        BxLine l = new BxLine();
        l.setBounds(new BxBounds(0, 0, 1, 1));
        l.addWord(w);

        BxZone z = new BxZone();
        z.setBounds(new BxBounds(10, 20, 30, 45));
        z.setLabel(BxZoneLabel.MET_AUTHOR);
        z.addLine(l);

        BxPage p = new BxPage();
        p.setBounds(new BxBounds(0, 0, 100, 100));
        p.addZone(z);

        BxPage p2 = new BxPage();
        p2.addZone(new BxZone().setLabel(BxZoneLabel.MET_ABSTRACT));
        p2.addZone(new BxZone().setLabel(BxZoneLabel.MET_AFFILIATION));
        p2.addZone(new BxZone().setLabel(BxZoneLabel.MET_AUTHOR));
        p2.addZone(new BxZone().setLabel(BxZoneLabel.GEN_BODY));
        p2.addZone(new BxZone().setLabel(BxZoneLabel.OTH_FOOTER));
        p2.addZone(new BxZone().setLabel(BxZoneLabel.OTH_HEADER));
        p2.addZone(new BxZone().setLabel(BxZoneLabel.MET_TITLE));
        p2.addZone(new BxZone().setLabel(BxZoneLabel.OTH_UNKNOWN));
        p2.addZone(new BxZone());

        BxDocument doc = new BxDocument();
        doc.addPage(p);
        doc.addPage(p2);
        return doc;
    }

    private Document bxToDom(BxDocument doc) throws Exception {
        BxDocumentToTrueVizWriter writer = new BxDocumentToTrueVizWriter();
        String out = writer.write(doc.getPages());
        return TrueVizUtils.newDocumentBuilder(true).parse(new InputSource(new StringReader(out)));
    }

    @Test
    public void testAppendCharacter() throws Exception {
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/Character[1]/CharacterCorners)", 1);

        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/Character[1]/CharacterID)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/Character[1]/CharacterNext)", 1);

        assertXpath("string(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/Character[1]/GT_Text/@Value)", "a");
        assertXpath("string(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/Character[2]/GT_Text/@Value)", "b");
    }

    @Test
    public void testAppendWord() throws Exception {
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/WordCorners)", 1);

        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/WordID)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/WordNext)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/WordNumChars)", 1);

        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word[1]/Character)", 2);
    }

    @Test
    public void testAppendLine() throws Exception {
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/LineCorners)", 1);

        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/LineID)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/LineNext)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/LineNumChars)", 1);

        assertXpath("count(/Document/Page[1]/Zone[1]/Line[1]/Word)", 1);
    }

    @Test
    public void testAppendZone() throws Exception {
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneCorners)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/Classification)", 1);
        assertXpathNoCase("string(/Document/Page[1]/Zone[1]/Classification/Category/@Value)", "Author");

        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneID)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneInsets)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneInsets/@Left)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneInsets/@Right)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneInsets/@Top)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneInsets/@Bottom)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneNext)", 1);
        assertXpath("count(/Document/Page[1]/Zone[1]/ZoneLines)", 1);

        assertXpath("count(/Document/Page[1]/Zone[1]/Line)", 1);

        assertXpathNoCase("string(/Document/Page[2]/Zone[1]/Classification/Category/@Value)", "Abstract");
        assertXpathNoCase("string(/Document/Page[2]/Zone[2]/Classification/Category/@Value)", "Affiliation");
        assertXpathNoCase("string(/Document/Page[2]/Zone[3]/Classification/Category/@Value)", "Author");
        assertXpathNoCase("string(/Document/Page[2]/Zone[4]/Classification/Category/@Value)", "Body");
        assertXpathNoCase("string(/Document/Page[2]/Zone[5]/Classification/Category/@Value)", "Footer");
        assertXpathNoCase("string(/Document/Page[2]/Zone[6]/Classification/Category/@Value)", "Header");
        assertXpathNoCase("string(/Document/Page[2]/Zone[7]/Classification/Category/@Value)", "Title");
        assertXpathNoCase("string(/Document/Page[2]/Zone[8]/Classification/Category/@Value)", "unknown");
        assertXpathNoCase("count(/Document/Page[2]/Zone[9]/Classification)", "0");
    }

    @Test
    public void testAppendPage() throws Exception {
        assertXpath("count(/Document/Page[1]/Zone)", 1);

        assertXpath("count(/Document/Page[1]/PageID)", 1);
        assertXpath("count(/Document/Page[1]/PageType)", 1);
        assertXpath("count(/Document/Page[1]/PageNumber)", 1);
        assertXpath("count(/Document/Page[1]/PageColumns)", 1);
        assertXpath("count(/Document/Page[1]/PageNext)", 1);
        assertXpath("count(/Document/Page[1]/PageZones)", 1);
    }

    @Test
    public void testAppendBounds() throws Exception {
        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[1]/@x)", 10);
        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[1]/@y)", 20);

        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[2]/@x)", 40);
        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[2]/@y)", 20);

        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[3]/@x)", 40);
        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[3]/@y)", 65);

        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[4]/@x)", 10);
        assertXpath("number(/Document/Page[1]/Zone[1]/ZoneCorners/Vertex[4]/@y)", 65);

        assertXpath("count(/Document/Page[2]/Zone[1]/ZoneCorners)", 1);
    }
}
