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

	@Override
	public void visitTypeInsn(int opcode, String type) {
		if (type.startsWith("[")) {
			dependencyGraphBuilder.processTypeSignature(type);
		} else {
			dependencyGraphBuilder.registerDependency(getClassByInternalName(type));
		}
	}

}