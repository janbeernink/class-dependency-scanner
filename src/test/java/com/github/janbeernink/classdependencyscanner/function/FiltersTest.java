package com.github.janbeernink.classdependencyscanner.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

public class FiltersTest {

	private static final Filter ALWAYS_TRUE = new Filter() {

		@Override
		public boolean includeClassInResults(Class<?> clazz) {
			return true;
		}
	};

	private static final Filter ALWAYS_FALSE = new Filter() {

		@Override
		public boolean includeClassInResults(Class<?> clazz) {
			return false;
		}
	};

	@Test
	public void testLimitToPackage() {
		Filter filter = Filters.limitToPackage("java.util");

		assertTrue(filter.includeClassInResults(List.class));
		assertFalse(filter.includeClassInResults(java.awt.List.class));
		assertTrue(filter.includeClassInResults(Logger.class));
		assertFalse(filter.includeClassInResults(String.class));
	}

	@Test
	public void testNot() {
		assertTrue(Filters.not(ALWAYS_FALSE).includeClassInResults(Object.class));
		assertFalse(Filters.not(ALWAYS_TRUE).includeClassInResults(Object.class));
	}

	@Test
	public void testOr() {
		assertTrue(Filters.or(ALWAYS_TRUE, ALWAYS_FALSE).includeClassInResults(Object.class));
		assertTrue(Filters.or(ALWAYS_FALSE, ALWAYS_TRUE).includeClassInResults(Object.class));
		assertFalse(Filters.or(ALWAYS_FALSE, ALWAYS_FALSE).includeClassInResults(Object.class));
	}

	@Test
	public void testAnd() {
		assertTrue(Filters.and(ALWAYS_TRUE, ALWAYS_TRUE).includeClassInResults(Object.class));
		assertFalse(Filters.and(ALWAYS_FALSE, ALWAYS_TRUE).includeClassInResults(Object.class));
		assertFalse(Filters.and(ALWAYS_TRUE, ALWAYS_FALSE).includeClassInResults(Object.class));
	}
}
