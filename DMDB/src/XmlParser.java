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
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by David on 23-Feb-17.
 */
public class XmlParser {
    public static String[] parse(String xmlString) {
        StringBuilder retsb = new StringBuilder();  //TODO: hantera < och såklart &gt; som Andreas sade
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlString)));
            Node rootNode = doc.getDocumentElement();
            if (rootNode.getNodeName().equals("message")) {
                String name = rootNode.getAttributes().getNamedItem("sender").getNodeValue();
                String colorString = "#000000";   //standardfärg svart
                NodeList nodeList = rootNode.getChildNodes();
                Node nodeItem;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    nodeItem = nodeList.item(i);


                    if (nodeItem.getNodeName().equals("text")) {
                        colorString = nodeItem.getAttributes().getNamedItem("color").getNodeValue();
                        retsb.append(nodeItem.getTextContent());
                    } else if (nodeItem.getNodeName().equals("encrypted")) {
                        
                    }
                }
                //retString = innerXml(doc.getDocumentElement());

                NodeList encryptedList = doc.getElementsByTagName("encrypted");
                if (encryptedList.getLength() > 0) {
                   // String[] retParts = retString.split("<encrypted>");
                    //retParts =
                    //dekryptera
                }

                return new String[]{retsb.toString(), name, colorString};


            }
        } catch (ParserConfigurationException e) {
            return handleFaults();   // TODO: output "Trasigt meddelande!"
        } catch (SAXException e) {
            return handleFaults();
        } catch (IOException e) {
            return handleFaults();
        }
        return handleFaults();
    }

    public static String parseRequest(String xmlString) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlString)));
            Node rootNode = doc.getDocumentElement();
            if (rootNode.getNodeName().equals("request")) {
                return rootNode.getTextContent();
            }
        } catch (ParserConfigurationException e) {
        return "";
        } catch (SAXException e) {
        return "";
        } catch (IOException e) {
        return "";
        }
        return "";
    }

    private static String innerXml(Node node) {          // returnerar all text under node som string, från stackoverflow
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

    private static String[] handleFaults(){
        return new String[]{"Nu kom det ett trasigt meddelande!", "System", "#7c7777"};
    }
}
