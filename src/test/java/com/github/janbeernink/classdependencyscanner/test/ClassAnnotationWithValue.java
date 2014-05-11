package com.github.janbeernink.classdependencyscanner.test;

import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;

@Retention(CLASS)
public @interface ClassAnnotationWithValue {

	Class<?> value() default DefaultClassAnnotationValueType.class;

}
