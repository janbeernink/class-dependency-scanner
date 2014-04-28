package com.github.janbeernink.classdependencyscanner.function;

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
