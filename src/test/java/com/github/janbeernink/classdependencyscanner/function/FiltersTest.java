package com.github.janbeernink.classdependencyscanner.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

public class FiltersTest {

	@Test
	public void testLimitToPackage() {
		Filter filter = Filters.limitToPackage("java.util");

		assertTrue(filter.includeClassInResults(List.class));
		assertFalse(filter.includeClassInResults(java.awt.List.class));
		assertTrue(filter.includeClassInResults(Logger.class));
		assertFalse(filter.includeClassInResults(String.class));
	}
}
