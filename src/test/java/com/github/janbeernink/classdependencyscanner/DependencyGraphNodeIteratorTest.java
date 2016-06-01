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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import com.github.janbeernink.classdependencyscanner.fielddependency.A;
import com.github.janbeernink.classdependencyscanner.fielddependency.B;

public class DependencyGraphNodeIteratorTest {

	@Test
	public void testSingleNode() {
		DependencyGraphNode node = new DependencyGraphNode(A.class);
		Iterator<DependencyGraphNode> iterator = node.iterator();

		assertTrue(iterator.hasNext());

		assertEquals(node, iterator.next());

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testNodeWithDependency() {
		DependencyGraphNode node = new DependencyGraphNode(A.class);
		DependencyGraphNode dependencyNode = new DependencyGraphNode(B.class);

		node.getDependencies().add(dependencyNode);

		Iterator<DependencyGraphNode> iterator = node.iterator();

		assertTrue(iterator.hasNext());

		assertEquals(node, iterator.next());

		assertTrue(iterator.hasNext());

		assertEquals(dependencyNode, iterator.next());

		assertFalse(iterator.hasNext());
	}
}
