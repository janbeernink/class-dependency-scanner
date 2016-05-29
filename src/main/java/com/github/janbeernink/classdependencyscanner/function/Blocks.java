/*
 * Copyright 2016 Jan Beernink
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.janbeernink.classdependencyscanner.function;

import java.util.Collection;

import com.github.janbeernink.classdependencyscanner.DependencyGraphNode;

public final class Blocks {

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
				collection.add(dependencyGraphNode.getNodeClass());
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
