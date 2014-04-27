package com.github.janbeernink.classdependencyscanner.function;

import com.github.janbeernink.classdependencyscanner.DependencyGraphNode;

public interface Block {

	void accept(DependencyGraphNode dependencyGraphNode);

}
