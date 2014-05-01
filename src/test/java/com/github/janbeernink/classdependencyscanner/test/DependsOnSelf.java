package com.github.janbeernink.classdependencyscanner.test;

public class DependsOnSelf {

	private DependsOnSelf dependsOnSelf;

	public DependsOnSelf(DependsOnSelf dependsOnSelf) {
		this.dependsOnSelf = dependsOnSelf;
	}

	public DependsOnSelf getDependsOnSelf() {
		return dependsOnSelf;
	}

	public void setDependsOnSelf(DependsOnSelf dependsOnSelf) {
		this.dependsOnSelf = dependsOnSelf;
	}
}
