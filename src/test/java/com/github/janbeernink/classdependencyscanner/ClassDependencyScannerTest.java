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

import static com.github.janbeernink.classdependencyscanner.Matchers.hasDependencies;
import static com.github.janbeernink.classdependencyscanner.function.Filters.isNotPartOfJDK;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsNot;
import org.junit.Test;

import com.github.janbeernink.classdependencyscanner.annotationdependency.AnnotatedClass;
import com.github.janbeernink.classdependencyscanner.annotationdependency.MyAnnotation;
import com.github.janbeernink.classdependencyscanner.fielddependency.A;
import com.github.janbeernink.classdependencyscanner.fielddependency.B;
import com.github.janbeernink.classdependencyscanner.fielddependency.C;
import com.github.janbeernink.classdependencyscanner.fielddependency.D;
import com.github.janbeernink.classdependencyscanner.function.Filters;
import com.github.janbeernink.classdependencyscanner.methoddependency.ClassWithMethod;
import com.github.janbeernink.classdependencyscanner.methoddependency.FirstParameterType;
import com.github.janbeernink.classdependencyscanner.methoddependency.ReturnType;
import com.github.janbeernink.classdependencyscanner.methoddependency.SecondParameterType;
import com.github.janbeernink.classdependencyscanner.methoddependency.VariableType;
import com.github.janbeernink.classdependencyscanner.supertypedependency.ChildClass;
import com.github.janbeernink.classdependencyscanner.supertypedependency.FirstInterfaceType;
import com.github.janbeernink.classdependencyscanner.supertypedependency.InterfaceImplementerType;
import com.github.janbeernink.classdependencyscanner.supertypedependency.SecondInterfaceType;
import com.github.janbeernink.classdependencyscanner.supertypedependency.SuperClass;
import com.github.janbeernink.classdependencyscanner.test.ClassUsingGenerics;
import com.github.janbeernink.classdependencyscanner.test.DependsOnSelf;
import com.github.janbeernink.classdependencyscanner.test.FieldType;
import com.github.janbeernink.classdependencyscanner.test.Interface;
import com.github.janbeernink.classdependencyscanner.test.InterfaceImplementation;
import com.github.janbeernink.classdependencyscanner.test.InterfaceTypeFactory;
import com.github.janbeernink.classdependencyscanner.throwsdependency.ClassWithThrowsMethod;
import com.github.janbeernink.classdependencyscanner.throwsdependency.TestException;

public class ClassDependencyScannerTest {

	@Test
	public void testDependency() {
		DependencyGraphNode dependencyGraph = new ClassDependencyScanner().buildDependencyGraph(A.class);

		assertEquals(A.class, dependencyGraph.getNodeClass());

		assertThat(dependencyGraph, hasDependencies(B.class, C.class, Object.class));

		dependencyGraph = new ClassDependencyScanner().setFilter(Filters.isNotPartOfJDK()).buildDependencyGraph(D.class);

		assertEquals(D.class, dependencyGraph.getNodeClass());

		assertThat(dependencyGraph.getDependencies(), hasItem(new DependencyGraphNode(A.class)));
		assertThat(dependencyGraph.getDependencies(), hasSize(1));

		assertThat(dependencyGraph.getDependencies().iterator().next(), hasDependencies(B.class, C.class));
	}

	@Test
	public void testInterfaceSuperTypes() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(InterfaceImplementerType.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItems(new DependencyGraphNode(FirstInterfaceType.class), new DependencyGraphNode(SecondInterfaceType.class)));
	}

	@Test
	public void testSuperClassDependency() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(ChildClass.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(SuperClass.class)));
	}

	@Test
	public void testMethodDependencies() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(ClassWithMethod.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItems(new DependencyGraphNode(VariableType.class), new DependencyGraphNode(FirstParameterType.class), new DependencyGraphNode(SecondParameterType.class), new DependencyGraphNode(ReturnType.class)));
	}

	@Test
	public void testThrowsClause() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(ClassWithThrowsMethod.class);

		assertThat(dependencyGraphNode, hasDependencies(TestException.class));
	}

	@Test
	public void testAnnotationDependencies() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(AnnotatedClass.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(MyAnnotation.class)));
	}

	@Test
	public void testDependsOnSelf() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().setFilter(Filters.isNotPartOfJDK()).buildDependencyGraph(DependsOnSelf.class);

		assertThat(dependencyGraphNode.getDependencies(), IsNot.not(hasItems(new DependencyGraphNode(DependsOnSelf.class))));
	}

	@Test
	public void testGenerics() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().setFilter(Filters.isNotPartOfJDK()).buildDependencyGraph(ClassUsingGenerics.class);

		assertThat(dependencyGraphNode, hasDependencies(ReturnType.class, FirstParameterType.class, FieldType.class, VariableType.class));
	}

	@Test
	public void testConstructorCall() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().setFilter(isNotPartOfJDK()).buildDependencyGraph(InterfaceTypeFactory.class);

		assertThat(dependencyGraphNode, hasDependencies(Interface.class, InterfaceImplementation.class));
	}
}
