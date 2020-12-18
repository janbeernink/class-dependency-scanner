/*
 * Copyright 2016-2020 Jan Beernink
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

import static com.github.janbeernink.classdependencyscanner.Util.getInputStreamForClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

import com.github.janbeernink.classdependencyscanner.function.Filter;

class DependencyGraphBuilder {

	static final int ASM_VERSION = Opcodes.ASM5;

	private final ClassVisitor classVisitor;
	private final MethodVisitor methodVisitor;
	private final SignatureVisitor signatureVisitor;

	private final Filter filter;

	private final Queue<Class<?>> typeQueue;

	private Map<Class<?>, DependencyGraphNode> dependencyGraphNodes;

	private DependencyGraphNode currentNode;

	DependencyGraphBuilder(Filter filter) {
		this.filter = filter;

		this.typeQueue = new LinkedList<>();
		this.dependencyGraphNodes = new HashMap<>();

		this.classVisitor = new DependencyClassVisitor(this);
		this.methodVisitor = new DependencyMethodVisitor(this);
		this.signatureVisitor = new DependencySignatureVisitor(this);
	}

	DependencyGraphNode buildDependencyGraph(Class<?> startingClass) {
		if (filter != null && !filter.include(startingClass)) {
			throw new IllegalArgumentException("Class " + startingClass.getName() + " is an invalid starting point as it's excluded by the filters");
		}

		DependencyGraphNode firstNode = new DependencyGraphNode(startingClass);
		dependencyGraphNodes.put(startingClass, firstNode);
		typeQueue.add(startingClass);

		try {
			while (!typeQueue.isEmpty()) {
				Class<?> currentClass = typeQueue.remove();
				currentNode = dependencyGraphNodes.get(currentClass);
				try (InputStream in = getInputStreamForClass(currentClass)) {
					ClassReader classReader = new ClassReader(in);

					classReader.accept(classVisitor, 0);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return firstNode;
	}

	void processMethodOrClassSignature(String signature) {
		new SignatureReader(signature).accept(signatureVisitor);
	}

	void processTypeSignature(String signature) {
		new SignatureReader(signature).acceptType(signatureVisitor);
	}

	MethodVisitor getMethodVisitor() {
		return methodVisitor;
	}

	void registerDependency(Class<?> type) {
		if (!type.isPrimitive() && filter.include(type) && !currentNode.getNodeClass().equals(type)) {
			if (!dependencyGraphNodes.containsKey(type)) {
				dependencyGraphNodes.put(type, new DependencyGraphNode(type));
				typeQueue.add(type);
			}

			currentNode.getDependencies().add(dependencyGraphNodes.get(type));
		}
	}
}
