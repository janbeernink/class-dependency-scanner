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
