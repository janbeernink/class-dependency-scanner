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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

public class FiltersTest {

	private static final Filter ALWAYS_TRUE = new Filter() {

		@Override
		public boolean include(Class<?> clazz) {
			return true;
		}
	};

	private static final Filter ALWAYS_FALSE = new Filter() {

		@Override
		public boolean include(Class<?> clazz) {
			return false;
		}
	};

	@Test
	public void testIsInPackage() {
		Filter filter = Filters.isInPackage("java.util");

		assertTrue(filter.include(List.class));
		assertFalse(filter.include(java.awt.List.class));
		assertTrue(filter.include(Logger.class));
		assertFalse(filter.include(String.class));
	}

	@Test
	public void testIsOneOf() {
		Filter filter = Filters.isOneOf(List.class);

		assertTrue(filter.include(List.class));
		assertFalse(filter.include(Object.class));
	}

	@Test
	public void isNotOneOf() {
		Filter filter = Filters.isNotOneOf(List.class);

		assertFalse(filter.include(List.class));
		assertTrue(filter.include(Object.class));
	}

	@Test
	public void testNot() {
		assertTrue(Filters.not(ALWAYS_FALSE).include(Object.class));
		assertFalse(Filters.not(ALWAYS_TRUE).include(Object.class));
	}

	@Test
	public void testOr() {
		assertTrue(Filters.or(ALWAYS_TRUE, ALWAYS_FALSE).include(Object.class));
		assertTrue(Filters.or(ALWAYS_FALSE, ALWAYS_TRUE).include(Object.class));
		assertFalse(Filters.or(ALWAYS_FALSE, ALWAYS_FALSE).include(Object.class));
	}

	@Test
	public void testAnd() {
		assertTrue(Filters.and(ALWAYS_TRUE, ALWAYS_TRUE).include(Object.class));
		assertFalse(Filters.and(ALWAYS_FALSE, ALWAYS_TRUE).include(Object.class));
		assertFalse(Filters.and(ALWAYS_TRUE, ALWAYS_FALSE).include(Object.class));
	}
}
