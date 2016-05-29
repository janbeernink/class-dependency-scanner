/*
 * Copyright 2016 Jan Beernink
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.janbeernink.classdependencyscanner;

import static com.github.janbeernink.classdependencyscanner.DependencyGraphBuilder.ASM_VERSION;
import static com.github.janbeernink.classdependencyscanner.Util.getClassByInternalName;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

final class DependencyClassVisitor extends ClassVisitor {

	private final DependencyGraphBuilder dependencyGraphBuilder;

	DependencyClassVisitor(DependencyGraphBuilder dependencyGraphBuilder) {
		super(ASM_VERSION);

		this.dependencyGraphBuilder = dependencyGraphBuilder;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		dependencyGraphBuilder.processTypeSignature(desc);

		return super.visitAnnotation(desc, visible);
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