package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.ClassPathEntry.ClassPathEntryType.DIRECTORY;
import static com.github.janbeernink.classdependencyscanner.ClassPathEntry.ClassPathEntryType.JAR;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;

import com.github.janbeernink.classdependencyscanner.function.Filter;

public class Util {

	private static final Pattern METHOD_DESCRIPTOR_PATTERN = Pattern
	        .compile("\\((?<parameterTypes>\\[*(?:Z|C|B|S|I|F|J|D|L.*;))*\\)\\[*(?<returnType>Z|C|B|S|I|F|J|D|V|L.*;)");

	private Util() {
	}

	static InputStream getInputStreamForClass(Class<?> clazz) {
		return Util.class.getResourceAsStream(getClassFileName(clazz));
	}

	static String getClassFileName(Class<?> clazz) {
		return "/" + clazz.getName().replace('.', '/') + ".class";
	}

	static Class<?> parseTypeDescriptor(String typeDescriptor) {
		String processedTypeDescriptor = stripArrayDeclarations(typeDescriptor);
		switch (processedTypeDescriptor) {
		case "Z":
			return Boolean.TYPE;
		case "C":
			return Character.TYPE;
		case "B":
			return Byte.TYPE;
		case "S":
			return Short.TYPE;
		case "I":
			return Integer.TYPE;
		case "F":
			return Float.TYPE;
		case "J":
			return Long.TYPE;
		case "D":
			return Double.TYPE;
		default:
			if (processedTypeDescriptor.matches("L.*;")) {
				return getClassByInternalName(processedTypeDescriptor.substring(1, processedTypeDescriptor.length() - 1));
			} else {
				throw new IllegalArgumentException("Invalid type descriptor: " + typeDescriptor);
			}
		}

	}

	static Set<Class<?>> parseMethodDescriptor(String methodDescriptor) {
		Set<Class<?>> classesFromMethodDescriptor = new HashSet<>();
		Matcher matcher = METHOD_DESCRIPTOR_PATTERN.matcher(methodDescriptor);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Not a valid method descriptor: " + methodDescriptor);
		}

		String parameterTypes = matcher.group("parameterTypes");

		if (parameterTypes != null) {
			for (int i = 0; i < parameterTypes.length(); i++) {

				if (parameterTypes.charAt(i) == 'L') {
					int indexOfSemicolon = parameterTypes.indexOf(';', i);

					classesFromMethodDescriptor.add(parseTypeDescriptor(parameterTypes.substring(i, indexOfSemicolon + 1)));

					i = indexOfSemicolon;
				} else if (parameterTypes.charAt(i) != '[') {
					classesFromMethodDescriptor.add(parseTypeDescriptor(parameterTypes.substring(i, i + 1)));
				}
			}
		}

		String returnType = matcher.group("returnType");

		if (!"V".equals(returnType)) {
			classesFromMethodDescriptor.add(parseTypeDescriptor(returnType));
		}

		return classesFromMethodDescriptor;
	}

	static Class<?> getClassByInternalName(String internalName) {
		try {
			return Class.forName(internalName.replace('/', '.'));
		} catch (ClassNotFoundException e) {
			// TODO use custom exception type
			throw new IllegalArgumentException(e);
		}
	}

	private static String stripArrayDeclarations(String typeDescriptor) {
		int i = 0;
		for (; i < typeDescriptor.length() && typeDescriptor.charAt(i) == '['; i++) {

		}
		if (i == 0) {
			return typeDescriptor;
		}
		return typeDescriptor.substring(i);
	}

	static Collection<Class<?>> parseTypeSignature(String typeSignature) {
		// TODO implement
		return Collections.emptySet();
	}

	static void processClass(Class<?> clazz, DependencyClassVisitor classVisitor) {
		try (InputStream in = getInputStreamForClass(clazz)) {
			ClassReader classReader = new ClassReader(in);

			classReader.accept(classVisitor, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void visitClass(Class<?> clazz, DependencyGraphNode currentNode, Map<Class<?>, DependencyGraphNode> visitedClasses, Filter filter) {
		if (!clazz.isPrimitive() && (filter == null || filter.include(clazz))) {
			if (visitedClasses.containsKey(clazz)) {
				currentNode.getDependencies().add(visitedClasses.get(clazz));
			} else {
				DependencyGraphNode nextNode = new DependencyGraphNode(clazz);

				currentNode.getDependencies().add(nextNode);
				visitedClasses.put(clazz, nextNode);

				processClass(clazz, new DependencyClassVisitor(nextNode, visitedClasses, filter));
			}
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
