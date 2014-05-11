package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.DependencyGraphBuilder.ASM_VERSION;
import static com.github.janbeernink.classdependencyscanner.Util.getClassByInternalName;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;


final class DependencyGraphMethodVisitor extends MethodVisitor {

	private final DependencyGraphBuilder dependencyGraphBuilder;

	DependencyGraphMethodVisitor(DependencyGraphBuilder dependencyGraphBuilder) {
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

	@Override
	public void visitTypeInsn(int opcode, String type) {
		if (type.startsWith("[")) {
			dependencyGraphBuilder.processTypeSignature(type);
		} else {
			dependencyGraphBuilder.registerDependency(getClassByInternalName(type));
		}
	}

}