package framework.selenium.objects;

import framework.Logger;
import java.util.HashMap;
import java.util.Map;

public class BaseMap<T> {
    private Map<String, T> entryMap;

    public BaseMap() {
        this.entryMap = new HashMap<>();
    }

    public T get(String name) {
        if (entryMap.containsKey(name)) {
            return entryMap.get(name);
        } else {
            Logger.getInstance().error(String.format("Element %s not found in collection %s", name, this.getClass().getSimpleName()));
            throw new NullPointerException();
        }
    }

    public void put(String name, T item) {
        entryMap.put(name, item);
    }
}
