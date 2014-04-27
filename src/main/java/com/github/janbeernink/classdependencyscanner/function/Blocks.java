package com.github.janbeernink.classdependencyscanner.function;

import java.util.Collection;

import com.github.janbeernink.classdependencyscanner.DependencyGraphNode;

public class Blocks {

	private Blocks() {
	}

	public static Block addClassTo(final Collection<? super Class<?>> collection) {
		return new Block() {

			@Override
			public void accept(DependencyGraphNode dependencyGraphNode) {
				collection.add(dependencyGraphNode.getDependencyClass());
			}
		};

	}
}
