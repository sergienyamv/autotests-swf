package framework;

import framework.dataproviders.PropertiesResourceManager;

public enum Environment {
	HEAVENS("heavens");

	String name;
	String dataPath, dictionariesPath, configPath, storagePath, locale;
	final String separator = System.getProperty("file.separator");

	Environment(String name) {
		String envPath = "environment" + separator + name;
		configPath = envPath + separator + "config.properties";
		PropertiesResourceManager envManager = new PropertiesResourceManager(configPath);
		String baseDir = System.getProperty("user.dir") != null ? System.getProperty("user.dir") : System.getProperty("project.basedir");
		String resourcePath = baseDir + separator + "src" + separator + "test" + separator + "resources" + separator;
		locale = envManager.getProperty("locale");
		dataPath = resourcePath + envPath + separator + envManager.getProperty("dataPath") + separator;
		dictionariesPath = resourcePath + envPath + separator + envManager.getProperty("dictionariesPath") + separator;
		storagePath = resourcePath + envPath + separator + envManager.getProperty("storagePath") + separator;
	}

	public String getConfigPath() {
		return configPath;
	}

	public String getDataPath() {
		return dataPath;
	}

	public String getDictionariesPath() {
		return dictionariesPath;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public String getLocale() {
		return locale;
	}

	public String getStartUrl() {
		return getEnvProperty("startUrl");
	}

	public String getEnvProperty(String property) {
		PropertiesResourceManager manager = new PropertiesResourceManager("selenium.properties");
		return new PropertiesResourceManager(Environment.valueOf(manager.getProperty("environment").toUpperCase()).getConfigPath()).getProperty(property);
	}
}