package com.aktimetrics.core.stereotypes;

import com.aktimetrics.core.transferobjects.Constants;
import com.aktimetrics.core.transferobjects.EventType;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Service
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EventHandler {
    EventType eventType();

    String name() default "";

    String version() default Constants.DEFAULT_VERSION;

}
