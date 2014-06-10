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
