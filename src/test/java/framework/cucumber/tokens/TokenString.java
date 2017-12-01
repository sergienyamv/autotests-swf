package framework.cucumber.tokens;

import framework.cucumber.transform.Processor;

public class TokenString {
    private Object value;

    public TokenString(String value) {
        this.value = Processor.dataTransform(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || value.getClass() != o.getClass()) return false;
        if (o instanceof String ) return o.equals(value);

        TokenString that = (TokenString) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}