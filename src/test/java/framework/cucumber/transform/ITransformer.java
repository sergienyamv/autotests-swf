package framework.cucumber.transform;

public interface ITransformer<T> {
    T transformData(String token);

    String getToken();
}