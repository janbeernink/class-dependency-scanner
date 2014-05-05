package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.DependencyClassVisitor.API_VERSION;
import static com.github.janbeernink.classdependencyscanner.Util.getClassByInternalName;
import static com.github.janbeernink.classdependencyscanner.Util.visitClass;

import java.util.Map;

import org.objectweb.asm.signature.SignatureVisitor;

import com.github.janbeernink.classdependencyscanner.function.Filter;

class DependencySignatureVisitor extends SignatureVisitor {


	private DependencyGraphNode currentNode;
	private Map<Class<?>, DependencyGraphNode> visitedClasses;
	private Filter filter;

	DependencySignatureVisitor(DependencyGraphNode currentNode, Map<Class<?>, DependencyGraphNode> visitedClasses, Filter filter) {
		super(API_VERSION);
		this.currentNode = currentNode;
		this.visitedClasses = visitedClasses;
		this.filter = filter;
	}


	@Override
	public void visitClassType(String name) {
		visitClass(getClassByInternalName(name), currentNode, visitedClasses, filter);
	}

}
