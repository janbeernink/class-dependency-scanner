package com.github.janbeernink.classdependencyscanner.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExcludeJDKClassesTest {

	@Test
	public void testJDKClass() {
		assertFalse(Filters.excludeJDKClasses().includeClassInResults(String.class));
	}

	@Test
	public void testNonJDKClass() {
		assertTrue(Filters.excludeJDKClasses().includeClassInResults(ExcludeJDKClassesTest.class));
	}
}