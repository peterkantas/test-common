package reading.xml;

import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XmlReader {

    public static boolean isIncludedTagNevTagErtek(Tier tier, Node xmlDocument, String tagName, String tagValue,String attName,String attValue,String...levels) {
        boolean result;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = Replacer.getExpression(tagName,attName,attValue,tier,levels);
            Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
            if (tagValue == null) {
                result = node != null;
            } else {
                result = node != null && node.getTextContent().equals(tagValue);
            }
            if (!result) {
                System.out.println("tagname: " + tagName + "|" + (node != null ? node.getTextContent() : "<empty>"));
            }
            return result;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getExpressionLevel(Tier tier) {
        return switch (tier) {
            case ONE -> "//*[local-name()='tagName']";
            case ONEATT -> "//*[local-name()='tagName'][@attributeName ='attributeValue']";
            case TWO ->
                    "//*[local-name()='level1']/*[local-name()='tagName']";
            case TWOATT ->
                    "//*[local-name()='level1']/*[local-name()='tagName'][@attributeName ='attributeValue']";
            case THREE ->
                    "//*[local-name()='level1']//*[local-name()='level2']/*[local-name()='tagName']";
            case THREEATT ->
                    "//*[local-name()='level1']//*[local-name()='level2']/*[local-name()='tagName'][@attributeName ='attributeValue']";
            case FOUR ->
                    "//*[local-name()='level1']//*[local-name()='level2']//*[local-name()='level3']/*[local-name()='tagName']";
            case FOURATT ->
                    "//*[local-name()='level1']//*[local-name()='level2']//*[local-name()='level3']/*[local-name()='tagName'][@attributeName ='attributeValue']";
            case FIVE ->
                    "//*[local-name()='level1']//*[local-name()='level2']//*[local-name()='level3']//*[local-name()='level4']/*[local-name()='tagName']";
            case FIVEATT ->
                    "//*[local-name()='level1']//*[local-name()='level2']//*[local-name()='level3']//*[local-name()='level4']/*[local-name()='tagName'][@attributeName ='attributeValue']";
            case SIX ->
                    "//*[local-name()='level1']//*[local-name()='level2']//*[local-name()='level3']//*[local-name()='level4']//*[local-name()='level5']/*[local-name()='tagName']";
            case SIXATT ->
                    "//*[local-name()='level1']//*[local-name()='level2']//*[local-name()='level3']//*[local-name()='level4']//*[local-name()='level5']/*[local-name()='tagName'][@attributeName ='attributeValue']";
        };
    }
}
