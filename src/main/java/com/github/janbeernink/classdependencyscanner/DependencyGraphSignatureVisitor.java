package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.DependencyGraphBuilder.ASM_VERSION;
import static com.github.janbeernink.classdependencyscanner.Util.getClassByInternalName;

import org.objectweb.asm.signature.SignatureVisitor;

class DependencyGraphSignatureVisitor extends SignatureVisitor {

	private final DependencyGraphBuilder dependencyGraphBuilder;

	DependencyGraphSignatureVisitor(DependencyGraphBuilder dependencyGraphBuilder) {
		super(ASM_VERSION);

		this.dependencyGraphBuilder = dependencyGraphBuilder;
	}

	@Override
	public void visitClassType(String name) {
		dependencyGraphBuilder.registerDependency(getClassByInternalName(name));
	}

}