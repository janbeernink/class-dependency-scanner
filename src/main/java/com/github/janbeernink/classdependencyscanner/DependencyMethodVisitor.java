package com.github.janbeernink.classdependencyscanner;

import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.github.janbeernink.classdependencyscanner.function.Filter;

final class DependencyMethodVisitor extends MethodVisitor {

	private DependencyGraphNode currentNode;
	private Map<Class<?>, DependencyGraphNode> visitedClasses;
	private Filter filter;

	DependencyMethodVisitor(DependencyGraphNode currentNode, Map<Class<?>, DependencyGraphNode> visitedClasses, Filter filter) {
		super(DependencyClassVisitor.API_VERSION);

		this.currentNode = currentNode;
		this.visitedClasses = visitedClasses;
		this.filter = filter;
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		if (!"this".equals(name)) {
			Util.visitClass(Util.parseTypeDescriptor(desc), currentNode, visitedClasses, filter);
		}
	}

}