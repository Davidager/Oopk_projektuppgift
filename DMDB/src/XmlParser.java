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
import java.io.ObjectInput;
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
                String colorString = "#000000";
                NodeList nodeList = rootNode.getChildNodes();
                Node nodeItem;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    nodeItem = nodeList.item(i);
                    Object[] retArray = partParse(nodeItem, retsb, name);
                    /*if (retArray[1].equals("keyrequest")) return new String[]{"", "", "request"};
                    retsb =(StringBuilder)retArray[0];
                    colorString = (String)retArray[1];


                    if (nodeItem.getNodeName().equals("text")) {
                        colorString = nodeItem.getAttributes().getNamedItem("color").getNodeValue();
                        retsb.append(nodeItem.getTextContent());
                    } else if (nodeItem.getNodeName().equals("encrypted")) {

                    } else if (nodeItem.getNodeName().equals("filerequest")){
                        String fileName = nodeItem.getAttributes().getNamedItem("name").getNodeValue();
                        String fileSize = nodeItem.getAttributes().getNamedItem("size").getNodeValue();
                        retsb.append(nodeItem.getTextContent());
                        String s = "krypto";
                        return new String[]{retsb.toString(), name,  fileName, fileSize, s};
                    }
                }
                //retString = innerXml(doc.getDocumentElement());

                NodeList encryptedList = doc.getElementsByTagName("encrypted");
                if (encryptedList.getLength() > 0) {
                   // String[] retParts = retString.split("<encrypted>");
                    //retParts =
                    //dekryptera */
                }

                return new String[]{retsb.toString(), name, colorString};


            }
        } catch (ParserConfigurationException e) {
            return handleFaults();   // TODO: output "Trasigt meddelande!"
        } catch (SAXException e) {
            return handleFaults();
        } catch (IOException e) {  // TODO: man kan fortfarande få bold och italic samtidigt
            return handleFaults();
        }
        return handleFaults();
    }

    private static Object[] partParse(Node nodeItem, StringBuilder sb, String name) throws IOException, SAXException, ParserConfigurationException {
        String colorString = "#000000";   //standardfärg svart
        if (nodeItem.getNodeName().equals("text")) {
            colorString = nodeItem.getAttributes().getNamedItem("color").getNodeValue();
            sb.append(nodeItem.getTextContent());
        } else if (nodeItem.getNodeName().equals("keyrequest")){
            return new Object[]{sb, "keyrequest"};    // om keyrequest
        } else if (nodeItem.getNodeName().equals("encrypted")) {
            String encryptionType = nodeItem.getAttributes().getNamedItem("type").getNodeValue();
            String decryptedSring;
            if (encryptionType.equals("AES")) {
                String encryptionKey = nodeItem.getAttributes().getNamedItem("key").getNodeValue();
                decryptedSring = EncryptionClass.decryptAES(encryptionKey, nodeItem.getTextContent());
            } else if (encryptionType.equals("caesar")) {
                String encryptionKey = nodeItem.getAttributes().getNamedItem("key").getNodeValue();
                decryptedSring = EncryptionClass.decryptCaesar(encryptionKey, nodeItem.getTextContent());

            } else return new Object[]{sb, colorString};
            decryptedSring = "<root>" + decryptedSring + "</root>";   // för att man ska kunna parsa
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(decryptedSring)));
            Node rootNode = doc.getDocumentElement();
            NodeList nodeList = rootNode.getChildNodes();
            Node node;
            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                Object[] retArray = partParse(node, sb, name);
                sb = (StringBuilder)retArray[0];
                colorString = (String) retArray[1];
            }
        } else if (nodeItem.getNodeName().equals("filerequest")){
            String fileName = nodeItem.getAttributes().getNamedItem("name").getNodeValue();
            String fileSize = nodeItem.getAttributes().getNamedItem("size").getNodeValue();
            sb.append(nodeItem.getTextContent());
            String s = "krypto";
            return new String[]{sb.toString(), name,  fileName, fileSize};
        }
        return new Object[]{sb, colorString};
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
