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
		return type == other.type && path.equals(other.path);
	}

}
