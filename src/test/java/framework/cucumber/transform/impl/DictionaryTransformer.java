package framework.cucumber.transform.impl;

import framework.common.functions.StringFunctions;
import framework.cucumber.transform.ITransformer;
import framework.dataproviders.XmlReader;
import framework.Configuration;

public class DictionaryTransformer implements ITransformer {
    @Override
    public String transformData(String token) {
        String fileName = StringFunctions.regexGetMatchGroup(token, "@dict\\.([\\S]+)\\.([\\S]+)", 1);
        String attrName = StringFunctions.regexGetMatchGroup(token, "@dict\\.([\\S]+)\\.([\\S]+)", 2);
        XmlReader reader = new XmlReader(Configuration.getCurrentEnvironment().getDictionariesPath() + fileName + ".xml");
        return reader.getNodeAtribute("//dictionary", attrName);
    }

    @Override
    public String getToken() {
        return "@dict";
    }
}
