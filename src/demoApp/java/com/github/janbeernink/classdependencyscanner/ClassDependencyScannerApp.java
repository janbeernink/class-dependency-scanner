package com.github.janbeernink.classdependencyscanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static com.github.janbeernink.classdependencyscanner.function.Blocks.addClassTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class ClassDependencyScannerApp {

	public static void main(String[] args) {
		ClassDependencyScanner classDependencyScanner = new ClassDependencyScanner();

		String className;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in, UTF_8))) {
			while ((className = readClassName(in)) != null) {

				try {
					Class<?> clazz = Class.forName(className);
					Set<Class<?>> sortedClasses = new TreeSet<>(new Comparator<Class<?>>() {

						@Override
						public int compare(Class<?> o1, Class<?> o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});

					classDependencyScanner.buildDependencyGraph(clazz).forEachNode(addClassTo(sortedClasses));

					System.out.println("The class \"" + className + "\" has the following dependencies:");
					System.out.println();

					for (Class<?> dependencyClass : sortedClasses) {
						System.out.println(dependencyClass.getName().replace('/', '.'));
					}
					System.out.println();

				} catch (ClassNotFoundException e) {
					System.err.println("Class \"" + className + "\" could not be found.");
				}

			}
		} catch (IOException e) {
			System.err.println("Unable to read from standard input: " + e.getMessage());
		}
	}

	private static String readClassName(BufferedReader in) throws IOException {
		System.out.println("Please enter a class name: ");
		return in.readLine();
	}

}
