package com.github.janbeernink.classdependencyscanner;

import java.nio.file.Path;

public class ClassPathEntry {
	public enum ClassPathEntryType {
		DIRECTORY, JAR
	}

	private Path path;
	private ClassPathEntryType type;

	public ClassPathEntry(Path path, ClassPathEntryType type) {
		this.path = path;
		this.type = type;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public ClassPathEntryType getType() {
		return type;
	}

	public void setType(ClassPathEntryType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return 31 * path.hashCode() + type.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ClassPathEntry other = (ClassPathEntry) obj;
		if (type != other.type || !path.equals(other.path)) {
			return false;
		}
		return true;
	}

}
