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