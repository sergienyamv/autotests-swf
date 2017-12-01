package framework.common.injections;

import framework.Logger;
import framework.common.assertextension.Assert;
import framework.selenium.objects.BasePage;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class IsPageOpenedInterceptor implements MethodInterceptor {
    private static final String METHOD_FINALIZE = "finalize";

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String methodName = methodInvocation.getMethod().getName();
        if (methodName.equals(METHOD_FINALIZE)) {
            return methodInvocation.proceed();
        }
        Object obj = methodInvocation.getThis();
        if (obj instanceof BasePage && !BasePage.isPageOpened(((BasePage) obj).uniqueLocator)) {
            Logger.getInstance().error("cannot create page, unique locator not found");
            Assert.fail("cannot create page, unique elements not found");
            throw new IllegalStateException("unique elements not found");
        }
        Object methodReturn = methodInvocation.proceed();
        return methodReturn;
    }
}
