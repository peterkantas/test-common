Feature list.:

#1 IgnoredSSL HTTP REQUEST (POST,GET etc..)

#2 PIPE REQUEST

#3 JAXB Validation

#4 File Reading(Excel,Json,XML)

Implementation examples.:

Excel Reader.: "Map<java.lang.Integer,
java.util.List<String>> asder = ExcelReader.readJExcel("src/test/resources/A.xls");
System.out.println(asder);"

Pipe Assertion.: "PipeAssert.pipeAssertion(response,numberOf,expectedValue);"

Json Assertion.: "JsonAssert.jsonAssert("APIENDPOINTSTRING","PARENT","CHILD",CHILDID(INT),"KEYVALUE",EXPECTEDVALUE);"

XML Assertion.: XmlReader.isIncludedTagNevTagErtek(Tier.ONEATT,valasz,"Uzenet",null,"ertek",szoveg,"");

HTTP Request.: "var response = communication.http.HttpHelper.sendPOSTRequest(URL, headers, requestString);"

TCPIP Request.: "sendPipeRequest(String Host, String HostPort, String bbOrg, String bbUser, String requestString)"

JAXB Validation.: "String responseString = TestXMLUtil.getStringFromDocument(response);
responseValidator(JarmuAltalanosValasz.class, xsdPaths, responseString);"


Versioning to jitpack.:
git tag -a 1.1.8 -m "v1.1.8"
git push origin --tag