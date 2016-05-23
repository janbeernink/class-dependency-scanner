package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.DependencyGraphBuilder.ASM_VERSION;
import static com.github.janbeernink.classdependencyscanner.Util.getClassByInternalName;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

final class DependencyGraphClassVisitor extends ClassVisitor {

	private final DependencyGraphBuilder dependencyGraphBuilder;

	DependencyGraphClassVisitor(DependencyGraphBuilder dependencyGraphBuilder) {
		super(ASM_VERSION);

		this.dependencyGraphBuilder = dependencyGraphBuilder;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		dependencyGraphBuilder.processTypeSignature(desc);

		return dependencyGraphBuilder.getAnnotationVisitor();
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (signature != null) {
			dependencyGraphBuilder.processMethodOrClassSignature(signature);
		} else {
			dependencyGraphBuilder.processMethodOrClassSignature(desc);
		}

		if (exceptions != null) {
			for (String exception : exceptions) {
				dependencyGraphBuilder.registerDependency(getClassByInternalName(exception));
			}
		}

		return dependencyGraphBuilder.getMethodVisitor();
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (signature != null) {
			dependencyGraphBuilder.processTypeSignature(signature);
		} else if (desc != null) {
			dependencyGraphBuilder.processTypeSignature(desc);
		}

		// TODO check in which cases value is set
		if (value != null) {
			dependencyGraphBuilder.registerDependency(value.getClass());
		}

		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		if (signature != null) {
			dependencyGraphBuilder.processMethodOrClassSignature(signature);
		} else if (superName != null) {
			dependencyGraphBuilder.registerDependency(getClassByInternalName(superName));
		}

		for (String interfaceName : interfaces) {
			dependencyGraphBuilder.registerDependency(getClassByInternalName(interfaceName));
		}
	}

}