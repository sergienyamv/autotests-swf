package framework.cucumber.transform.impl;

import framework.Logger;
import framework.common.functions.FileFunctions;
import framework.common.functions.StringFunctions;
import framework.cucumber.transform.ITransformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommonFunctionsTransformer implements ITransformer {
    private final String regexToken = "([\\s\\S]*)(" + getToken() + "\\.([\\w]+)\\(([\\S]+)\\))([\\s\\S]*)";
    private final String regexFunction = getToken() + "\\.([\\w]+)\\(([\\S]+)\\)";
    private final String regexArguments = getToken() + "\\.([\\w]+)\\(([\\S]+)\\)";
    private final String regexArgument = "'([\\S\\s]+)'";

    public String transformData(String token) {
        String beforeTokenMethod = StringFunctions.regexGetMatchGroup(token, regexToken, 1);
        String tokenMethod = StringFunctions.regexGetMatchGroup(token, regexToken, 2);
        String afterTokenMethod = StringFunctions.regexGetMatchGroup(token, regexToken, 5);
        String methodName = StringFunctions.regexGetMatchGroup(tokenMethod, regexFunction, 1);
        String[] arguments = StringFunctions.regexGetMatchGroup(tokenMethod, regexArguments, 2).split(",");
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = StringFunctions.regexGetMatchGroup(arguments[i], regexArgument, 1);
        }
        try {
            Method method = null;
            if (arguments.length == 1) {
                method = FileFunctions.class.getMethod(methodName, String.class);
            }
            if (arguments.length == 2) {
                method = FileFunctions.class.getMethod(methodName, String.class, String.class);
            }
            if (arguments.length == 3) {
                method = FileFunctions.class.getMethod(methodName, String.class, String.class, String.class);
            }
            return beforeTokenMethod + method.invoke(null, arguments) + afterTokenMethod;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Logger.getInstance().debug(e.getMessage());
        }
        return null;
    }

    @Override
    public String getToken() {
        return "@fileFunction";
    }
}
