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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.janbeernink.classdependencyscanner.function.Block;

public class DependencyGraphNode implements Iterable<DependencyGraphNode> {

	private Class<?> nodeClass;

	private ClassPathEntry classPathEntry;
	private Set<DependencyGraphNode> dependencies = new HashSet<>();

	public DependencyGraphNode(Class<?> nodeClass) {
		this(nodeClass, Util.getClassPathEntryByClass(nodeClass));
	}

	public DependencyGraphNode(Class<?> nodeClass, ClassPathEntry classPathEntry) {
		this.nodeClass = nodeClass;
		this.classPathEntry = classPathEntry;
	}

	public Class<?> getNodeClass() {
		return nodeClass;
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
		return nodeClass.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		DependencyGraphNode other = (DependencyGraphNode) obj;

		return getNodeClass().equals(other.getNodeClass()) && getClassPathEntry().equals(other.getClassPathEntry());
	}

	@Override
	public String toString() {
		return "DependencyGraphNode [nodeClass=" + nodeClass + ", classPathEntry=" + classPathEntry + "]";
	}
}
