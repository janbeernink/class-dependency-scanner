package com.github.janbeernink.classdependencyscanner;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

import com.github.janbeernink.classdependencyscanner.annotationdependency.AnnotatedClass;
import com.github.janbeernink.classdependencyscanner.annotationdependency.MyAnnotation;
import com.github.janbeernink.classdependencyscanner.fielddependency.A;
import com.github.janbeernink.classdependencyscanner.fielddependency.B;
import com.github.janbeernink.classdependencyscanner.fielddependency.C;
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
import com.github.janbeernink.classdependencyscanner.throwsdependency.ClassWithThrowsMethod;
import com.github.janbeernink.classdependencyscanner.throwsdependency.TestException;

public class ClassDependencyScannerTest {

	@Test
	public void testDependency() {
		DependencyGraphNode dependencyGraph = new ClassDependencyScanner().buildDependencyGraph(A.class);

		assertEquals(A.class, dependencyGraph.getDependencyClass());

		Set<DependencyGraphNode> dependencies = dependencyGraph.getDependencies();

		assertThat(dependencies, hasItem(new DependencyGraphNode(B.class)));
		assertThat(dependencies, hasItem(new DependencyGraphNode(C.class)));
	}

	@Test
	public void testInterfaceSuperTypes() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(InterfaceImplementerType.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(FirstInterfaceType.class)));
		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(SecondInterfaceType.class)));
	}

	@Test
	public void testSuperClassDependency() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(ChildClass.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(SuperClass.class)));
	}

	@Test
	public void testMethodDependencies() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(ClassWithMethod.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(VariableType.class)));
		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(FirstParameterType.class)));
		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(SecondParameterType.class)));
		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(ReturnType.class)));
	}

	@Test
	public void testThrowsClause() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(ClassWithThrowsMethod.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(TestException.class)));
	}

	@Test
	public void testAnnotationDependencies() {
		DependencyGraphNode dependencyGraphNode = new ClassDependencyScanner().buildDependencyGraph(AnnotatedClass.class);

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(MyAnnotation.class)));
	}
}
