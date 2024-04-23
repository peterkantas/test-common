package reading.xml

object Replacer {
    @JvmStatic
    fun getExpression(
        tagName: String,
        attName: String?,
        attValue: String?,
        tier: Tier,
        vararg levels: String
    ): String {
        var expression = XmlReader.getExpressionLevel(tier)
        expression = expression.replace("tagName", tagName)
        if (levels.size > 0) {
            expression = expression.replace("level1", levels[0])
        }
        if (levels.size > 1) {
            expression = expression.replace("level2", levels[1])
        }
        if (levels.size > 2) {
            expression = expression.replace("level3", levels[2])
        }
        if (levels.size > 3) {
            expression = expression.replace("level4", levels[3])
        }
        if (levels.size > 4) {
            expression = expression.replace("level5", levels[4])
        }
        if (levels.size > 5) {
            expression = expression.replace("level6", levels[5])
        }
        if (attName != null) {
            expression = expression.replace("attributeName", attName)
            expression = expression.replace("attributeValue", attValue!!)
        }
        return expression
    }
}
