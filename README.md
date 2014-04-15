Class Dependency Scanner
========================

This is a small experimental Java library to analyze the dependencies between classes and generate a dependency graph.

Usage
========================

The following example shows how to generate a dependency graph for class String:

    new ClassDependencyScanner().buildDependencyGraph(String.class);

This will return a DependencyGraphNode that represents the String class. This node can be used as a starting point to iterate over the entire graph:

    for (DependencyGraphNode dependencyGraphNode: startingNode) {
        // Do something with each node
    }

The iterator returned by the iterator() method on DependencyGraphNode is guaranteed to visit each node exactly once, even when there are cycles in the graph.

Known issues
========================

* Generic signatures are ignored.
* Annotations are not yet fully processed.

License
========================

This project is licensed under the LGPL version 2.1, please see the LICENSE file for more details.
