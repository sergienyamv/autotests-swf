package framework.genie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GenieServer {
    private GenieServer() {}

    private static Process serverProcess;
    private static InputStream err;
    private static InputStream inn;
    private static OutputStream out;

    public static void startServer(String[] params) {
        try {
            if (serverProcess == null) {
                serverProcess = Runtime.getRuntime().exec("java -jar target/test-classes/genie/GenieServer/GenieSocketServer.jar");
                err = serverProcess.getErrorStream();
                inn = serverProcess.getInputStream();
                out = serverProcess.getOutputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() throws IOException {
        if(serverProcess != null) {
            serverProcess.destroy();
            byte b[] = new byte[err.available()];
            err.read(b, 0, b.length);
            System.out.println(new String(b));
            byte in[] = new byte[inn.available()];
            err.read(in, 0, in.length);
            System.out.println(new String(in));
        }
    }
}
