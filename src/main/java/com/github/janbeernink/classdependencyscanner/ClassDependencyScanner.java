package com.github.janbeernink.classdependencyscanner;

import com.github.janbeernink.classdependencyscanner.function.Filter;

public class ClassDependencyScanner {

	private static final Filter INCLUDE_ALL_CLASSES = new Filter() {

		@Override
		public boolean include(Class<?> clazz) {
			return true;
		}

	};

	private Filter filter;

	/**
	 * Set a filter to use during the creation of the dependency graph.
	 *
	 * @param filter
	 *            the filter to use
	 * @return the current instance
	 */
	public ClassDependencyScanner setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}

	/**
	 * Build a dependency graph, starting at a given {@link Class}.
	 *
	 * @param startingPoint
	 *            the class to use as the starting point when building the graph
	 * @return a {@link DependencyGraphNode} representing the class that has been used as the starting point
	 */
	public DependencyGraphNode buildDependencyGraph(Class<?> startingPoint) {
		return new DependencyGraphBuilder(filter == null ? INCLUDE_ALL_CLASSES : filter).buildDependencyGraph(startingPoint);
	}

}
