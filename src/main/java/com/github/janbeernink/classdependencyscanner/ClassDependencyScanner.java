package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.Util.processClass;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.github.janbeernink.classdependencyscanner.function.Filter;

public class ClassDependencyScanner {
	static final Logger logger = Logger.getLogger(ClassDependencyScanner.class.getName());

	static final Pattern SIGNATURE_PATTERN = Pattern.compile("\\[?L(?<class>[^<;]+)(?:<.*>)?;");

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
		if (filter != null && !filter.include(startingPoint)) {
			// TODO throw exception
			return null;
		}

		DependencyGraphNode dependencyGraphNode = new DependencyGraphNode(startingPoint);

		processClass(dependencyGraphNode.getDependencyClass(), new DependencyClassVisitor(dependencyGraphNode,
		        new HashMap<Class<?>, DependencyGraphNode>(), filter));

		return dependencyGraphNode;
	}

}
