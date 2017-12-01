package framework.dataproviders;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import framework.Logger;

public class ProjectFilesProvider {
	private final static String delimiter = System.getProperty("file.separator");
	private final static String dirJsScripts = String.join(delimiter, new String[]{"src", "test", "java", "framework", "common", "javascripts", ""});

	/**
	 * returns absolute path to the project file relative to base project directory
     *
     * @param resourceRelativePath path to the resource relative to base directory
	 * @return absolute path to the project file relative to base project directory
     */
    public static String getFileAbsolutePath(String resourceRelativePath) {
        String baseDir = System.getProperty("user.dir") != null ? System.getProperty("user.dir") : System.getProperty("project.basedir");
		return baseDir + delimiter + resourceRelativePath;
	}

    private static String getFileContent(String path) {
        String resourcePath = getFileAbsolutePath(path);
		try {
			InputStream inStream = new FileInputStream(resourcePath);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
			String read;
            while ((read = br.readLine()) != null) {
                sb.append(read);
            }
			br.close();
			return sb.toString();
		} catch (IOException e) {
			Logger.getInstance().fatal("Cannot read file " + resourcePath);
			return null;
		}
	}

    /**
     * read content of js file placed in the src/test/java/framework/common/javascripts/
     *
     * @param name script name
	 * @return content of js file
	 */
    public static String getJavaScript(String name) {
        return getFileContent(dirJsScripts + name + ".js");
	}
}
