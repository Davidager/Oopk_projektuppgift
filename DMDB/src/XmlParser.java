import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by David on 23-Feb-17.
 */
public class XmlParser {
    private String communicationType;

    public String parse(String xmlString) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlString)));
            communicationType = doc.getDocumentElement().getNodeName();
            if (communicationType == "message") {

            }
        } catch (ParserConfigurationException e) {
            return handleFaults();
        } catch (SAXException e) {
            return handleFaults();
        } catch (IOException e) {
            return handleFaults();
        }
        return "asdasd";
    }

    private String innerXml(Node node) {          // returnerar all text under node som string, från stackoverflow
        DOMImplementationLS lsImpl = (DOMImplementationLS)node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        NodeList childNodes = node.getChildNodes();
        lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString();
    }

    private String handleFaults(){
        return "Nu kom det ett trasigt meddelande!";
    }
}
