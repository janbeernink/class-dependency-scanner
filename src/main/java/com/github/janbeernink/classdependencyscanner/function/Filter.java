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

public interface Filter {

	/**
	 * Returns if the given {@link Class} should be included in the dependency graph.
	 *
	 * @param clazz
	 *            the class to test
	 * @return <code>true</code> if the node should be included in the dependency graph and <code>false</code> otherwise
	 */
	boolean include(Class<?> clazz);
}
