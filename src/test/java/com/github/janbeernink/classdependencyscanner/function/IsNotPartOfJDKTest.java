package com.github.janbeernink.classdependencyscanner.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IsNotPartOfJDKTest {

	@Test
	public void testJDKClass() {
		assertFalse(Filters.isNotPartOfJDK().include(String.class));
	}

	@Test
	public void testNonJDKClass() {
		assertTrue(Filters.isNotPartOfJDK().include(IsNotPartOfJDKTest.class));
	}
}
