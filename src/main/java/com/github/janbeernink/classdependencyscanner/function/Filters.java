package com.github.janbeernink.classdependencyscanner.function;

import com.github.janbeernink.classdependencyscanner.ClassPathEntry;
import com.github.janbeernink.classdependencyscanner.Util;

public final class Filters {

	private static final Filter EXCLUDE_JDK_CLASSES_FILTER = new Filter() {
		private ClassPathEntry classPathEntry = Util.getClassPathEntryByClass(Object.class);

		@Override
		public boolean includeClassInResults(Class<?> clazz) {

			// This will probably break once the module system is introduced
			return !classPathEntry.equals(Util.getClassPathEntryByClass(clazz));
		}
	};

	private Filters() {
	}

	public static Filter excludeJDKClasses() {
		return EXCLUDE_JDK_CLASSES_FILTER;
	}

	public static Filter limitToPackage(final String packageName) {
		return new Filter() {

			@Override
			public boolean includeClassInResults(Class<?> clazz) {
				return clazz.getPackage().getName().equals(packageName) || clazz.getPackage().getName().startsWith(packageName + ".");
			}

		};
	}

	public static Filter and(final Filter firstFilter, final Filter secondFilter) {
		return new Filter() {

			@Override
			public boolean includeClassInResults(Class<?> clazz) {
				return firstFilter.includeClassInResults(clazz) && secondFilter.includeClassInResults(clazz);
			}

		};
	}
}