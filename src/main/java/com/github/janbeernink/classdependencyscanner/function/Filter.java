package com.github.janbeernink.classdependencyscanner.function;

public interface Filter {

	boolean include(Class<?> clazz);
}
