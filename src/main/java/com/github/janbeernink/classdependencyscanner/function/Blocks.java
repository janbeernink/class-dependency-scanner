package com.github.janbeernink.classdependencyscanner.function;

import java.util.Collection;

import com.github.janbeernink.classdependencyscanner.DependencyGraphNode;

public class Blocks {

	private Blocks() {
	}

	/**
	 * Returns a new {@link Block} that adds the class of the dependency graph node to the given collection.
	 *
	 * @param collection
	 *            the collection to add the class to
	 * @return a new block that adds the class to the collection
	 */
	public static Block addClassTo(final Collection<? super Class<?>> collection) {
		return new Block() {

			@Override
			public void accept(DependencyGraphNode dependencyGraphNode) {
				collection.add(dependencyGraphNode.getDependencyClass());
			}
		};

	}

	/**
	 * Returns a new {@link Block} that adds a dependency graph node to the given collection.
	 *
	 * @param collection
	 *            the collection to add the node to
	 * @return a new block that adds the node to the collection
	 */
	public static Block addNodeTo(final Collection<? super DependencyGraphNode> collection) {
		return new Block() {

			@Override
			public void accept(DependencyGraphNode dependencyGraphNode) {
				collection.add(dependencyGraphNode);
			}
		};
	}
}
