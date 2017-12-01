package framework.dataproviders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XmlReader {

	private final File file;

    public XmlReader(String pathToXml) {
        file = new File(pathToXml);
	}

    /**
     * provide node text by xpath expression
     *
     * @param xpathExpression like //serviceUri
	 * @return text from the node
	 */
    public String getNodeText(String xpathExpression) {
        try {
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
			InputSource inputSource = new InputSource(inputStream);
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node node = (Node) xpath.evaluate(xpathExpression, inputSource, XPathConstants.NODE);
			return node.getTextContent();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

    public String getNodeAtribute(String xpathExpression, String attrName) {
        try {
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
			InputSource inputSource = new InputSource(inputStream);
            XPath xpath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xpath.evaluate(xpathExpression, inputSource, XPathConstants.NODE);
            return node.getAttributes().getNamedItem(attrName).getNodeValue();
		} catch (FileNotFoundException | XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

    public int getNumberOfNoder(String xpathExpression) throws XPathExpressionException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(file.getAbsolutePath());
        InputSource inputSource = new InputSource(inputStream);
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xpath.evaluate(xpathExpression, inputSource, XPathConstants.NODESET);
		return nodes.getLength();
    }
}