package framework.selenium.objects.elements;

import framework.selenium.objects.BaseMap;

public class ElementMap<T extends Element> extends BaseMap<T> {
    public void addElement(T item) {
            put(item.getName(), item);
    }

    public T getElement(String name) {
        return get(name);
    }
}
