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
package com.github.janbeernink.classdependencyscanner.example;

import static java.nio.charset.StandardCharsets.UTF_8;
import static com.github.janbeernink.classdependencyscanner.function.Blocks.addClassTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.github.janbeernink.classdependencyscanner.ClassDependencyScanner;

public class DemoApp {

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
