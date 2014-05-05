package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.DependencyGraphBuilder.ASM_VERSION;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;


final class DependencyMethodVisitor extends MethodVisitor {

	private final DependencyGraphBuilder dependencyGraphBuilder;

	DependencyMethodVisitor(DependencyGraphBuilder dependencyGraphBuilder) {
		super(ASM_VERSION);

		this.dependencyGraphBuilder = dependencyGraphBuilder;
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		if (!"this".equals(name)) {
			if (signature != null) {
				dependencyGraphBuilder.processTypeSignature(signature);
			} else {
				dependencyGraphBuilder.processTypeSignature(desc);
			}
		}
	}

}