package framework.cucumber.transform;

import framework.cucumber.transform.impl.CommonFunctionsTransformer;
import framework.cucumber.transform.impl.DictionaryTransformer;
import framework.cucumber.transform.impl.TestDataTransformer;
import java.util.ArrayList;
import java.util.List;

public class Transformer {
    private final List<ITransformer> transformers;

	public Transformer(List<ITransformer> transformers) {
		this.transformers = transformers;
		transformers.add(new TestDataTransformer());
		transformers.add(new DictionaryTransformer());
		transformers.add(new CommonFunctionsTransformer());
	}

	public Transformer() {
		this(new ArrayList<>());
	}

	public String transform(String token) {
		for (ITransformer transformer : transformers) {
			if (token.contains(transformer.getToken())) {
				return (String) transformer.transformData(token);
			}
		}
		return token;
	}
}