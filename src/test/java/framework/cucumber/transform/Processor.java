package framework.cucumber.transform;

public class Processor extends cucumber.api.Transformer<String> {
    @Override
    public String transform(String token) {
        String result = new Transformer().transform(token);
        if (!result.equals(token))
            result = transform(result);
        return result;
    }

    public static String dataTransform(String token) {
        String result = new Transformer().transform(token);
        if (!result.equals(token))
            result = dataTransform(result);
        return result;
    }
}