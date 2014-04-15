package com.github.janbeernink.classdependencyscanner.function;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;

import com.github.janbeernink.classdependencyscanner.DependencyGraphNode;

public class BlocksTest {

	@Test
	public void testAddClassesToCollection() {
		List<Object> objects = new ArrayList<>();

		Block block = Blocks.addClassToCollection(objects);

		block.process(new DependencyGraphNode(BlocksTest.class));

		assertThat(objects, IsCollectionContaining.hasItem(BlocksTest.class));
	}
}
