/*
 * Copyright 2016-2020 Jan Beernink
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.janbeernink.classdependencyscanner.ClassPathEntry;
import com.github.janbeernink.classdependencyscanner.Util;

public final class Filters {

	private static final Filter EXCLUDE_JDK_CLASSES_FILTER = new Filter() {
		private ClassPathEntry classPathEntry = Util.getClassPathEntryByClass(Object.class);

		@Override
		public boolean include(Class<?> clazz) {

			// This will probably break once the module system is introduced
			return !classPathEntry.equals(Util.getClassPathEntryByClass(clazz));
		}
	};

	private Filters() {
	}

	/**
	 * Returns a filter that excludes classes from the standard JDK libraries from the dependeny graph
	 *
	 * @return a filter that excludes classes from the standard JDK libraries
	 */
	public static Filter isNotPartOfJDK() {
		return EXCLUDE_JDK_CLASSES_FILTER;
	}

	/**
	 * Returns a filter that only includes classes in a given package or any subpackages of this package.
	 *
	 * @param packageName
	 *            the name of the package
	 * @return a filter that only includes classes from a certain package
	 */
	public static Filter isInPackage(final String packageName) {
		return isInPackage(packageName, true);
	}

	/**
	 * Returns a filter that only includes classes in a given package and optionally all sub-packages of this package
	 *
	 * @param packageName
	 *            the name of the package
	 * @param includeSubPackages
	 *            boolean to indicate if sub-packages should be included or not
	 * @return a filter that only includes classes in a certain package
	 */
	public static Filter isInPackage(final String packageName, final boolean includeSubPackages) {
		return new Filter() {

			@Override
			public boolean include(Class<?> clazz) {
				return clazz.getPackage().getName().equals(packageName)
				        || (includeSubPackages && clazz.getPackage().getName().startsWith(packageName + "."));
			}

		};
	}

	/**
	 * Returns a filter that only returns `true` if a class is in a given set of classes.
	 *
	 * @param classes
	 *            the classes to include
	 * @return a filter that only returns `true` for the given classes and false otherwise
	 */
	public static Filter isOneOf(Class<?>... classes) {
		return isOneOf(Arrays.asList(classes));
	}

	/**
	 * Returns a filter that only returns `true` if a class is in a given set of classes.
	 *
	 * @param classes
	 *            the collection of classes to include
	 * @return a filter that only returns `true` for any class in the given collection of classes and false otherwise
	 */
	public static Filter isOneOf(Collection<Class<?>> classes) {
		final Set<Class<?>> includedClasses = Collections.unmodifiableSet(new HashSet<>(classes));

		return new Filter() {

			@Override
			public boolean include(Class<?> clazz) {
				return includedClasses.contains(clazz);
			}
		};
	}

	/**
	 * Returns a filter that only returns `false` if a class is in a given set of classes.
	 *
	 * @param classes
	 *            the classes to exclude
	 * @return a filter that only returns `false` for the given classes and `true` otherwise
	 */
	public static Filter isNotOneOf(Class<?>... classes) {
		return not(isOneOf(classes));
	}

	/**
	 * Returns a filter that only returns `false` if a class is in a given collection of classes.
	 *
	 * @param classes
	 *            the collection of classes to exclude
	 * @return a filter that only returns `false` for any class in the given collection of classes and `true` otherwise
	 */
	public static Filter isNotOneOf(Collection<Class<?>> classes) {
		return not(isOneOf(classes));
	}

	/**
	 * Returns a composed filter that performs a short-circuiting logical AND of two filters.
	 *
	 * @param firstFilter
	 *            the first filter
	 * @param secondFilter
	 *            the second filter
	 * @return the composed filter
	 */
	public static Filter and(final Filter firstFilter, final Filter secondFilter) {
		return new Filter() {

			@Override
			public boolean include(Class<?> clazz) {
				return firstFilter.include(clazz) && secondFilter.include(clazz);
			}

		};
	}

	/**
	 * Returns a composed filter that performs a short-circuiting logical OR of two filters.
	 *
	 * @param firstFilter
	 *            the first filter
	 * @param secondFilter
	 *            the second filter
	 * @return the composed filter
	 */
	public static Filter or(final Filter firstFilter, final Filter secondFilter) {
		return new Filter() {
			@Override
			public boolean include(Class<?> clazz) {
				return firstFilter.include(clazz) || secondFilter.include(clazz);
			}
		};
	}

	/**
	 * Returns a filter that negates the result of the input filter
	 *
	 * @param filter
	 *            the filter that should be negated
	 * @return the negated filter
	 */
	public static Filter not(final Filter filter) {
		return new Filter() {
			@Override
			public boolean include(Class<?> clazz) {
				return !filter.include(clazz);
			}
		};
	}
}
