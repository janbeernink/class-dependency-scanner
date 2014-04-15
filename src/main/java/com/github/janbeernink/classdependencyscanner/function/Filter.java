package com.github.janbeernink.classdependencyscanner.function;

public interface Filter {

	boolean includeClassInResults(Class<?> clazz);
}
