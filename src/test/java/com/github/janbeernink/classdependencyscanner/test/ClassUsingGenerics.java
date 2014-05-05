package com.github.janbeernink.classdependencyscanner.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.janbeernink.classdependencyscanner.methoddependency.FirstParameterType;
import com.github.janbeernink.classdependencyscanner.methoddependency.ReturnType;
import com.github.janbeernink.classdependencyscanner.methoddependency.VariableType;

public class ClassUsingGenerics<T> {

	public List<FieldType> list;

	public List<ReturnType> getList() {
		return Collections.emptyList();
	}

	public void put(double d, List<FirstParameterType> list, int i) {
	}

	public void someMethod() {
		List<VariableType> list = new ArrayList<>();
		list.toString();
	}
}
