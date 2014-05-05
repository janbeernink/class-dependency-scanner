package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.Util.getClassByInternalName;
import static com.github.janbeernink.classdependencyscanner.Util.parseMethodDescriptor;
import static com.github.janbeernink.classdependencyscanner.Util.parseTypeDescriptor;
import static com.github.janbeernink.classdependencyscanner.Util.visitClass;

import java.util.Map;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;

import com.github.janbeernink.classdependencyscanner.function.Filter;

final class DependencyClassVisitor extends ClassVisitor {

	static final int API_VERSION = Opcodes.ASM5;

	private DependencyGraphNode currentNode;
	private Map<Class<?>, DependencyGraphNode> visitedClasses;
	private Filter filter;

	DependencyClassVisitor(DependencyGraphNode dependencyGraphNode, Map<Class<?>, DependencyGraphNode> visitedClasses, Filter filter) {
		super(API_VERSION);

		this.currentNode = dependencyGraphNode;
		this.visitedClasses = visitedClasses;
		this.filter = filter;

	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		visitClass(parseTypeDescriptor(desc), currentNode, visitedClasses, filter);

		return super.visitAnnotation(desc, visible);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (signature != null) {
			new SignatureReader(signature).accept(new DependencySignatureVisitor(currentNode, visitedClasses, filter));
		} else {
			Set<Class<?>> classesFromMethodDescriptor = parseMethodDescriptor(desc);
			for (Class<?> clazz : classesFromMethodDescriptor) {
				visitClass(clazz, currentNode, visitedClasses, filter);
			}
		}

		if (exceptions != null) {
			for (String exception : exceptions) {
				visitClass(getClassByInternalName(exception), currentNode, visitedClasses, filter);
			}
		}

		return new DependencyMethodVisitor(currentNode, visitedClasses, filter);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (signature != null) {
			new SignatureReader(signature).accept(new DependencySignatureVisitor(currentNode, visitedClasses, filter));
		} else if (desc != null) {
			visitClass(parseTypeDescriptor(desc), currentNode, visitedClasses, filter);
		}

		// TODO check in which cases value is set
		if (value != null) {
			visitClass(value.getClass(), currentNode, visitedClasses, filter);
		}

		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// TODO parse signature first and only parse superName and interfaces if the signature is null

		if (superName != null) {
			visitClass(getClassByInternalName(superName), currentNode, visitedClasses, filter);
		}

		for (String interfaceName : interfaces) {
			visitClass(getClassByInternalName(interfaceName), currentNode, visitedClasses, filter);
		}
	}
}