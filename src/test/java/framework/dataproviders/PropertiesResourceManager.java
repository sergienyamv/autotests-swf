package framework.dataproviders;

import framework.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class-wrapper for working with properties-файлами Для доступа к файлам используется относительный путь (имя ресурса)
 */
public final class PropertiesResourceManager {

    private static final Logger logger = Logger.getInstance();

	/**
	 * @uml.property name="properties"
	 */
	private Properties properties = new Properties();

	/**
     * Default Constructor
     */
    public PropertiesResourceManager() {
		properties = new Properties();
	}

	/**
	 * Constructor
     *
     * @param resourceName Name of resource
	 */
	public PropertiesResourceManager(final String resourceName) {
		properties = appendFromResource(properties, resourceName);
	}

	/**
	 * Конструктор для создания одного объекта из двух properties-файлов
     *
     * @param defaultResourceName Default Resource Name
     * @param resourceName        Resource Name
     */
	public PropertiesResourceManager(final String defaultResourceName, final String resourceName) {
		this(defaultResourceName);
		properties = appendFromResource(new Properties(properties), resourceName);
	}

	/**
	 * Объединение двух properties-файлов (параметры из 2-го файла переопределяют параметры из 1-го)
     *
     * @param objProperties Properties
     * @param resourceName  Resource Name
     * @return Properties
	 */
	private Properties appendFromResource(final Properties objProperties, final String resourceName) {
        try {
            String resourcePath = ProjectFilesProvider.getFileAbsolutePath("/src/test/resources/" + resourceName);
			InputStream inStream = new FileInputStream(resourcePath);
			if (inStream != null) {
				objProperties.load(inStream);
				inStream.close();
			}
			return objProperties;
        } catch (FileNotFoundException exp) {
            logger.error(String.format("Resource '%1$s' could not be found", resourceName));
			exp.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return objProperties;
	}

	/**
	 * Получение значения параметра по ключу
     *
     * @param key Key
	 * @return Value
	 */
	public String getProperty(final String key) {
		return properties.getProperty(key);
	}

	/**
	 * Получение значения параметра по ключу
     *
     * @param key          Key
     * @param defaultValue Default Value
	 * @return Value
	 */
	public String getProperty(final String key, final String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Sets the property
     *
     * @param key   Key
     * @param value Value
	 */
	public void setProperty(final String key, final String value) {
		properties.setProperty(key, value);
	}
}