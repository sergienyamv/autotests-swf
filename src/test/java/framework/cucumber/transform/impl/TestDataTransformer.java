package framework.cucumber.transform.impl;

import framework.Configuration;
import framework.common.functions.StringFunctions;
import framework.cucumber.transform.ITransformer;
import framework.dataproviders.XmlReader;

public class TestDataTransformer implements ITransformer {
    @Override
    public String transformData(String token) {
        String fileName = StringFunctions.regexGetMatchGroup(token, getToken() + "\\.([\\S]+?)\\.([\\S]+)", 1);
        String xPath = "//" + StringFunctions.regexGetMatchGroup(token, getToken() + "\\.([\\S]+?)\\.([\\S]+)", 2).replace(".", "//");
        XmlReader reader = new XmlReader(Configuration.getCurrentEnvironment().getDataPath() + fileName + ".xml");
        return reader.getNodeText(xPath);
    }

    @Override
    public String getToken() {
        return "@testData";
    }
}
