package com.github.janbeernink.classdependencyscanner;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.janbeernink.classdependencyscanner.function.Block;

public class DependencyGraphNode implements Iterable<DependencyGraphNode> {

	private Class<?> dependencyClass;

	private ClassPathEntry classPathEntry;
	private Set<DependencyGraphNode> dependencies = new HashSet<>();

	public DependencyGraphNode(Class<?> dependencyClass) {
		this(dependencyClass, Util.getClassPathEntryByClass(dependencyClass));
	}

	public DependencyGraphNode(Class<?> dependencyClass, ClassPathEntry classPathEntry) {
		this.dependencyClass = dependencyClass;
		this.classPathEntry = classPathEntry;
	}

	public Class<?> getDependencyClass() {
		return dependencyClass;
	}

	public ClassPathEntry getClassPathEntry() {
		return classPathEntry;
	}

	public Set<DependencyGraphNode> getDependencies() {
		return dependencies;
	}

	@Override
	public Iterator<DependencyGraphNode> iterator() {
		return new DependencyGraphNodeIterator(this);
	}

	public void forEachNode(Block block) {
		for (DependencyGraphNode dependencyGraphNode : this) {
			block.accept(dependencyGraphNode);
		}
	}

	@Override
	public int hashCode() {
		return dependencyClass.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		DependencyGraphNode other = (DependencyGraphNode) obj;

		return getDependencyClass().equals(other.getDependencyClass()) && getClassPathEntry().equals(other.getClassPathEntry());
	}

	@Override
	public String toString() {
		return "DependencyGraphNode [dependencyClass=" + dependencyClass + ", classPathEntry=" + classPathEntry + "]";
	}
}
