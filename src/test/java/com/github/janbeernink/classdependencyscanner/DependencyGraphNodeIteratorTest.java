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
