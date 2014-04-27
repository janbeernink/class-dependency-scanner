package com.github.janbeernink.classdependencyscanner.function;

public interface Filter {

	/**
	 * Returns if the given {@link Class} should be included in the dependency graph.
	 *
	 * @param clazz
	 *            the class to test
	 * @return <code>true</code> if the node should be included in the dependency graph and <code>false</code> otherwise
	 */
	boolean include(Class<?> clazz);
}
