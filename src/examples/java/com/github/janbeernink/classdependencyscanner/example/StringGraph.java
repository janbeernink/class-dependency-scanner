package com.github.janbeernink.classdependencyscanner.example;

import com.github.janbeernink.classdependencyscanner.ClassDependencyScanner;
import com.github.janbeernink.classdependencyscanner.DependencyGraphNode;

public class StringGraph {

	public static void main(String[] args) {
		// tag::graphCreation[]
		DependencyGraphNode startingNode = new ClassDependencyScanner().buildDependencyGraph(String.class);
		// end::graphCreation[]

		// tag::iterate[]
		for (DependencyGraphNode node: startingNode) {
			// Do something useful
		}
		// end::iterate[]
	}
}
