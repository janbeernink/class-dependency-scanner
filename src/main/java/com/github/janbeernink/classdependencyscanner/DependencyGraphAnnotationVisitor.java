package com.github.janbeernink.classdependencyscanner;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

class DependencyGraphAnnotationVisitor extends AnnotationVisitor {

	private final DependencyGraphBuilder dependencyGraphBuilder;

	DependencyGraphAnnotationVisitor(DependencyGraphBuilder dependencyGraphBuilder) {
		super(DependencyGraphBuilder.ASM_VERSION);
		this.dependencyGraphBuilder = dependencyGraphBuilder;
	}

	@Override
	public void visit(String name, Object value) {
		if (value instanceof Type) {
			String className = ((Type) value).getClassName();
			try {
				Class<?> type = Class.forName(className);

				dependencyGraphBuilder.registerDependency(type);

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
