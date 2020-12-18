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
package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.ClassPathEntry.ClassPathEntryType.DIRECTORY;
import static com.github.janbeernink.classdependencyscanner.ClassPathEntry.ClassPathEntryType.JAR;
import static com.github.janbeernink.classdependencyscanner.Util.getClassPathEntryByClass;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testGetClassPathEntryByClass() {
		ClassPathEntry runtimeClassPathEntry = getClassPathEntryByClass(String.class);
		assertEquals(JAR, runtimeClassPathEntry.getType());
		assertTrue(exists(runtimeClassPathEntry.getPath()));

		ClassPathEntry directoryClassPathEntry = getClassPathEntryByClass(UtilTest.class);
		assertEquals(DIRECTORY, directoryClassPathEntry.getType());
		assertTrue(exists(directoryClassPathEntry.getPath()));
		assertTrue(isDirectory(directoryClassPathEntry.getPath()));
	}
}
