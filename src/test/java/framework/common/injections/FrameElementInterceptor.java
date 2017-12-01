package framework.common.injections;

import framework.Logger;
import framework.selenium.Browser;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.ArrayList;
import java.util.Objects;

public class FrameElementInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String targetFrame = methodInvocation.getMethod().getAnnotation(IFrameElement.class).value();
        String[] targetFrames = methodInvocation.getMethod().getAnnotation(IFrameElement.class).values();
        if (!targetFrame.equals("[null]")) {
            Logger.getInstance().debug(String.format("Switch to :: Frame %s", targetFrame));
            Browser.getInstance().switchToFrame(targetFrame);
        }
        else {
            for (String name :
                    targetFrames) {
                Logger.getInstance().debug(String.format("Switch to :: Frame %s", name));
                Browser.getInstance().switchToFrame(name);
            }
        }
        try {
            return methodInvocation.proceed();
        }
        finally {
            Logger.getInstance().debug("Switch to :: Default Content");
            Browser.getInstance().switchToDefaultContent();
        }
    }
}
