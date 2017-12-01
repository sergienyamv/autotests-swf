package framework.store;

import framework.Configuration;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Store {

    public static <T> void store(T object, String name) {
        XMLEncoder e = null;

        try {
            //e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(Configuration.getCurrentEnvironment().getStoragePath() + Store.class.getSimpleName() + "_" + name + ".xml")));
            FileOutputStream fos = new FileOutputStream(Configuration.getCurrentEnvironment().getStoragePath() + Store.class.getSimpleName() + "_" + name + ".xml");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            e = new XMLEncoder(bos);
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        }

        e.writeObject(object);
        e.close();
    }

    public static <T> T get(String name) {
        XMLDecoder xmlDecoder = null;

        try {
            xmlDecoder = new XMLDecoder(new FileInputStream(Configuration.getCurrentEnvironment().getStoragePath() + Store.class.getSimpleName() + "_" + name + ".xml"));
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        }

        Object object = xmlDecoder.readObject();
        xmlDecoder.close();
        return (T) object;
    }
}
