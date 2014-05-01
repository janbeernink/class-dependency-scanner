package com.github.janbeernink.classdependencyscanner;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Set;

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
import com.github.janbeernink.classdependencyscanner.test.DependsOnSelf;
import com.github.janbeernink.classdependencyscanner.throwsdependency.ClassWithThrowsMethod;
import com.github.janbeernink.classdependencyscanner.throwsdependency.TestException;
import org.hamcrest.core.IsNot;
import org.junit.Test;

public class ClassDependencyScannerTest {

	@Test
	public void testDependency() {
		DependencyGraphNode dependencyGraph = new ClassDependencyScanner().buildDependencyGraph(A.class);

		assertEquals(A.class, dependencyGraph.getNodeClass());

		Set<DependencyGraphNode> dependencies = dependencyGraph.getDependencies();

		assertThat(dependencies, hasItems(new DependencyGraphNode(B.class), new DependencyGraphNode(C.class), new DependencyGraphNode(Object.class)));

		dependencyGraph = new ClassDependencyScanner().setFilter(Filters.isNotPartOfJDK()).buildDependencyGraph(D.class);

		assertEquals(D.class, dependencyGraph.getNodeClass());

		assertThat(dependencyGraph.getDependencies(), hasItem(new DependencyGraphNode(A.class)));
		assertThat(dependencyGraph.getDependencies(), hasSize(1));

		assertThat(dependencyGraph.getDependencies().iterator().next().getDependencies(), hasItems(new DependencyGraphNode(B.class), new DependencyGraphNode(C.class)));
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

		assertThat(dependencyGraphNode.getDependencies(), hasItem(new DependencyGraphNode(TestException.class)));
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
}
