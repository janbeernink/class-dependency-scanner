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
package com.github.janbeernink.classdependencyscanner;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;

public final class Matchers {

	private Matchers() {
	}

	public static Matcher<DependencyGraphNode> hasDependencies(final Class<?>... classes) {
		DependencyGraphNode[] nodes = new DependencyGraphNode[classes.length];
		for (int i = 0; i < classes.length; i++) {
			nodes[i] = new DependencyGraphNode(classes[i]);
		}

		final Matcher<Iterable<DependencyGraphNode>> matcher = IsCollectionContaining.hasItems(nodes);

		return new BaseMatcher<DependencyGraphNode>() {

			@Override
			public boolean matches(Object item) {
				return matcher.matches(((DependencyGraphNode) item).getDependencies());
			}

			@Override
			public void describeMismatch(Object item, Description description) {
				List<String> dependencies = new ArrayList<>();
				for (DependencyGraphNode node: ((DependencyGraphNode)item).getDependencies()) {
					dependencies.add(node.getNodeClass().getName());
				}

				description.appendValueList("depends on: ", ", ", ".", dependencies);
			}

			@Override
			public void describeTo(Description description) {
				List<String> classNames = new ArrayList<>();
				for (Class<?> clazz : classes) {
					classNames.add(clazz.getName());
				}

				description.appendValueList("depends on: ", ", ", ".", classNames);
			}
		};
	}
}
