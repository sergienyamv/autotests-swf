package framework;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import framework.common.injections.PageInjectModule;
import framework.selenium.objects.BaseMap;

public class Application {
    private Injector pageInjector = Guice.createInjector(new PageInjectModule());
    private static BaseMap<Object> store = new BaseMap<>();

    public <T> T getPage(Class<T> key) {
        return pageInjector.getInstance(key);
    }

    public <T> Provider<T> getPageProvider(Class<T> key) {
        return pageInjector.getProvider(key);
    }

    public void storeObject(String name, Object item) {
        store.put(name, item);
    }

    public Object getObject(String name) {
        return store.get(name);
    }
}