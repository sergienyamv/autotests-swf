package framework.common.functions;

import java.io.File;
import java.util.*;

public final class FileFunctions {
    /**
     * Returns a reference to a file with the specified name that is located
     * somewhere on the path.
     */
    public static File findFileOnPath(final String fileName, final String path) {
        final String classpath = path;
        final String pathSeparator = System.getProperty("path.separator");
        final StringTokenizer tokenizer = new StringTokenizer(classpath, pathSeparator);
        while (tokenizer.hasMoreTokens()) {
            final String pathElement = tokenizer.nextToken();
            final File directoryOrJar = new File(pathElement);
            final File absoluteDirectoryOrJar = directoryOrJar.getAbsoluteFile();
            if (absoluteDirectoryOrJar.isFile()) {
                final File target = new File(absoluteDirectoryOrJar.getParent(), fileName);
                if (target.exists()) {
                    return target;
                }
            } else {
                final File target = new File(directoryOrJar, fileName);
                if (target.exists()) {
                    return target;
                }
            }
        }
        return null;
    }
}