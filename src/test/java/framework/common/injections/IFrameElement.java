package framework.common.injections;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface IFrameElement {
    String value() default "[null]";
    String[] values() default "[null]";
}
