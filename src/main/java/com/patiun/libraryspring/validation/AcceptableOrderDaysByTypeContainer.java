package com.patiun.libraryspring.validation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AcceptableOrderDaysByTypeContainer {

    AcceptableOrderDaysByType[] value();
}
