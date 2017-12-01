package framework.common.injections;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import framework.selenium.objects.BasePage;

public class PageInjectModule extends AbstractModule {
    @Override
    protected void configure() {
        //bindInterceptor(Matchers.subclassesOf(BasePage.class), Matchers.any(), new IsPageOpenedInterceptor());
        bindInterceptor(Matchers.subclassesOf(BasePage.class), Matchers.annotatedWith(IFrameElement.class), new FrameElementInterceptor());
    }
}