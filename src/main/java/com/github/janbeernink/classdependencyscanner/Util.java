package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.ClassPathEntry.ClassPathEntryType.DIRECTORY;
import static com.github.janbeernink.classdependencyscanner.ClassPathEntry.ClassPathEntryType.JAR;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

public class Util {

	private Util() {
	}

	static InputStream getInputStreamForClass(Class<?> clazz) {
		return Util.class.getResourceAsStream(getClassFileName(clazz));
	}

	static String getClassFileName(Class<?> clazz) {
		return "/" + clazz.getName().replace('.', '/') + ".class";
	}

	static Class<?> getClassByInternalName(String internalName) {
		try {
			return Class.forName(internalName.replace('/', '.'));
		} catch (ClassNotFoundException e) {
			// TODO use custom exception type
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Get the class path entry for a given {@link Class}.
	 *
	 * @param clazz
	 *            the class to get the class path entry for
	 * @return the class path entry
	 */
	public static ClassPathEntry getClassPathEntryByClass(Class<?> clazz) {
		URL resource = Util.class.getResource(getClassFileName(clazz));

		if ("jar".equals(resource.getProtocol())) {
			String filePart = resource.getFile();
			int indexOfExclamationMark = filePart.lastIndexOf('!');
			return new ClassPathEntry(Paths.get(filePart.substring(5, indexOfExclamationMark)), JAR);

		} else if ("file".equals(resource.getProtocol())) {
			String filePart = resource.getFile();

			String classFileName = getClassFileName(clazz);
			if (filePart.endsWith(classFileName)) {
				return new ClassPathEntry(Paths.get(filePart.substring(0, filePart.length() - classFileName.length())), DIRECTORY);
			}
		}

		throw new IllegalArgumentException("Unkown classpath entry type");
	}
}
