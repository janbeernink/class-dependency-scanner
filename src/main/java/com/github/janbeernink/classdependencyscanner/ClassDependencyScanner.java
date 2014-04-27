package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.Util.processClass;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.github.janbeernink.classdependencyscanner.function.Filter;

/**
 * @author janbeernink
 */
public class ClassDependencyScanner {
	static final Logger logger = Logger.getLogger(ClassDependencyScanner.class.getName());

	static final Pattern SIGNATURE_PATTERN = Pattern.compile("\\[?L(?<class>[^<;]+)(?:<.*>)?;");

	private Filter filter;

	public ClassDependencyScanner setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public DependencyGraphNode buildDependencyGraph(Class<?> startingPoint) {
		if (filter != null && !filter.include(startingPoint)) {
			// TODO throw exception
			return null;
		}

		DependencyGraphNode dependencyGraphNode = new DependencyGraphNode(startingPoint);

		processClass(dependencyGraphNode.getDependencyClass(), new DependencyClassVisitor(dependencyGraphNode, new HashMap<Class<?>, DependencyGraphNode>(), filter));

		return dependencyGraphNode;
	}

}
