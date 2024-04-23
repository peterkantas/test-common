package reading.xml;

public class Replacer {

    public static String getExpression(String tagName, String attName, String attValue, Tier tier, String... levels) {
        String expression = XmlReader.getExpressionLevel(tier);
        expression = expression.replace("tagName", tagName);
        if (levels.length > 0) {
            expression = expression.replace("level1", levels[0]);
        }  if (levels.length > 1) {
            expression = expression.replace("level2", levels[1]);
        }  if (levels.length > 2) {
            expression = expression.replace("level3", levels[2]);
        }if (levels.length > 3) {
            expression = expression.replace("level4", levels[3]);
        }if (levels.length > 4) {
            expression = expression.replace("level5", levels[4]);
        }if (levels.length > 5) {
            expression = expression.replace("level6", levels[5]);
        }
        if (attName != null) {
            expression = expression.replace("attributeName", attName);
            expression = expression.replace("attributeValue", attValue);
        }
        return expression;
    }
}
