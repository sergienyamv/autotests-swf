package framework.common.assertextension;

import framework.Logger;
import framework.cucumber.tokens.TokenString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Assert extends org.testng.Assert {

    public static void assertEquals(String actual, TokenString expected, String message) {
        assertEquals(actual, expected.toString(), message);
    }

    public static void assertEquals(Collection<?> actual, List<TokenString> expected, String message) {
        List<String> list = expected.stream().map(TokenString::toString).collect(Collectors.toList());
        assertEquals(actual, list, message);
    }

    public static <T> void isTrueForAll(Predicate<T> func, List<T> elements, String messageTemplate) {
        List<String> result = new ArrayList<>();
        for (T element : elements) {
            if (!func.test(element)) {
                result.add(String.format(messageTemplate, element.toString()));
            }
        }
        if (result.size() > 0) {
            Logger.getInstance().fatal(String.join("\n", result));
        }
    }
}
