package framework.cucumber.tokens;

import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import framework.cucumber.transform.Processor;

@XStreamConverter(Processor.class)
public class TokenDataTable { }
