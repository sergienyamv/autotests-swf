package framework;

import framework.dataproviders.PropertiesResourceManager;

public class Configuration {
	private static Environment currentEnvironment;

	public static Environment getCurrentEnvironment() {
		if (currentEnvironment != null) {
			return currentEnvironment;
		}
		PropertiesResourceManager confManager = new PropertiesResourceManager("selenium.properties");
		String localEnv = confManager.getProperty("environment");
		currentEnvironment = Environment.valueOf(System.getProperty("environment") != null ? System.getProperty("environment").toUpperCase() : localEnv.toUpperCase());
		return currentEnvironment;
	}
}
