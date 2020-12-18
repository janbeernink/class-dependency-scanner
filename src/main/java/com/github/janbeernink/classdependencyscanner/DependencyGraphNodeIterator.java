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
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

class DependencyGraphNodeIterator implements Iterator<DependencyGraphNode> {

	private Stack<LinkedList<DependencyGraphNode>> dependencyGraphNodes = new Stack<>();
	private Set<DependencyGraphNode> visitedNodes = new HashSet<>();

	public DependencyGraphNodeIterator(DependencyGraphNode dependencyGraphNode) {
		LinkedList<DependencyGraphNode> dependencyList = new LinkedList<>();
		dependencyList.add(dependencyGraphNode);
		dependencyGraphNodes.add(dependencyList);
	}

	@Override
	public boolean hasNext() {
		return findNextNode();
	}

	@Override
	public DependencyGraphNode next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		DependencyGraphNode nextNode = dependencyGraphNodes.peek().removeFirst();

		dependencyGraphNodes.push(new LinkedList<>(nextNode.getDependencies()));

		visitedNodes.add(nextNode);

		return nextNode;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	private boolean findNextNode() {
		while(!dependencyGraphNodes.isEmpty()) {
			LinkedList<DependencyGraphNode> dependencyGraphNodeList = dependencyGraphNodes.peek();
			while(!dependencyGraphNodeList.isEmpty() && visitedNodes.contains(dependencyGraphNodeList.getFirst())) {
				dependencyGraphNodeList.removeFirst();
			}

			if(dependencyGraphNodeList.isEmpty()) {
				dependencyGraphNodes.pop();
			} else {
				return true;
			}
		}
		return false;
	}
}