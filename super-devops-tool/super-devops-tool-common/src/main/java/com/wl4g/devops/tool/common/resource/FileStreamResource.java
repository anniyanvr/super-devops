/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wl4g.devops.tool.common.resource;

/**
 * Retention of upstream license agreement statement:</br>
 * Thank you very much spring framework, We fully comply with and support the open license
 * agreement of spring. The purpose of migration is to solve the problem
 * that these elegant API programs can still be easily used without running
 * in the spring environment.
 * </br>
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import com.wl4g.devops.tool.common.lang.Assert2;
import com.wl4g.devops.tool.common.lang.StringUtils2;

/**
 * {@link org.springframework.core.io.FileSystemResource} implementation for
 * {@code java.io.File} handles. Supports resolution as a {@code File} and also
 * as a {@code URL}. Implements the extended {@link WritableStreamResource}
 * interface.
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see java.io.File
 */
public class FileStreamResource extends AbstractStreamResource {

	private final File file;

	private final String path;

	/**
	 * Create a new {@code FileSystemResource} from a {@link File} handle.
	 * <p>
	 * Note: When building relative resources via {@link #createRelative}, the
	 * relative path will apply <i>at the same directory level</i>: e.g. new
	 * File("C:/dir1"), relative path "dir2" -> "C:/dir2"! If you prefer to have
	 * relative paths built underneath the given root directory, use the
	 * {@link #FileSystemResource(String) constructor with a file path} to
	 * append a trailing slash to the root path: "C:/dir1/", which indicates
	 * this directory as root for all relative paths.
	 * 
	 * @param file
	 *            a File handle
	 */
	public FileStreamResource(File file) {
		Assert2.notNull(file, "File must not be null");
		this.file = file;
		this.path = StringUtils2.cleanPath(file.getPath());
	}

	/**
	 * Create a new {@code FileSystemResource} from a file path.
	 * <p>
	 * Note: When building relative resources via {@link #createRelative}, it
	 * makes a difference whether the specified resource base path here ends
	 * with a slash or not. In the case of "C:/dir1/", relative paths will be
	 * built underneath that root: e.g. relative path "dir2" -> "C:/dir1/dir2".
	 * In the case of "C:/dir1", relative paths will apply at the same directory
	 * level: relative path "dir2" -> "C:/dir2".
	 * 
	 * @param path
	 *            a file path
	 */
	public FileStreamResource(String path) {
		Assert2.notNull(path, "Path must not be null");
		this.file = new File(path);
		this.path = StringUtils2.cleanPath(path);
	}

	/**
	 * Return the file path for this resource.
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * This implementation returns whether the underlying file exists.
	 * 
	 * @see java.io.File#exists()
	 */
	@Override
	public boolean exists() {
		return this.file.exists();
	}

	/**
	 * This implementation checks whether the underlying file is marked as
	 * readable (and corresponds to an actual file with content, not to a
	 * directory).
	 * 
	 * @see java.io.File#canRead()
	 * @see java.io.File#isDirectory()
	 */
	@Override
	public boolean isReadable() {
		return (this.file.canRead() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileInputStream for the underlying file.
	 * 
	 * @see java.io.FileInputStream
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	/**
	 * This implementation checks whether the underlying file is marked as
	 * writable (and corresponds to an actual file with content, not to a
	 * directory).
	 * 
	 * @see java.io.File#canWrite()
	 * @see java.io.File#isDirectory()
	 */
	public boolean isWritable() {
		return (this.file.canWrite() && !this.file.isDirectory());
	}

	/**
	 * This implementation opens a FileOutputStream for the underlying file.
	 * 
	 * @see java.io.FileOutputStream
	 */
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(this.file);
	}

	/**
	 * This implementation returns a URL for the underlying file.
	 * 
	 * @see java.io.File#toURI()
	 */
	@Override
	public URL getURL() throws IOException {
		return this.file.toURI().toURL();
	}

	/**
	 * This implementation returns a URI for the underlying file.
	 * 
	 * @see java.io.File#toURI()
	 */
	@Override
	public URI getURI() throws IOException {
		return this.file.toURI();
	}

	/**
	 * This implementation returns the underlying File reference.
	 */
	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * This implementation returns the underlying File's length.
	 */
	@Override
	public long contentLength() throws IOException {
		return this.file.length();
	}

	/**
	 * This implementation creates a FileSystemResource, applying the given path
	 * relative to the path of the underlying file of this resource descriptor.
	 * 
	 * @see org.springframework.util.StringUtils#applyRelativePath(String,
	 *      String)
	 */
	@Override
	public StreamResource createRelative(String relativePath) {
		String pathToUse = StringUtils2.applyRelativePath(this.path, relativePath);
		return new FileStreamResource(pathToUse);
	}

	/**
	 * This implementation returns the name of the file.
	 * 
	 * @see java.io.File#getName()
	 */
	@Override
	public String getFilename() {
		return this.file.getName();
	}

	/**
	 * This implementation returns a description that includes the absolute path
	 * of the file.
	 * 
	 * @see java.io.File#getAbsolutePath()
	 */
	@Override
	public String getDescription() {
		return "file [" + this.file.getAbsolutePath() + "]";
	}

	/**
	 * This implementation compares the underlying File references.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj == this || (obj instanceof FileStreamResource && this.path.equals(((FileStreamResource) obj).path)));
	}

	/**
	 * This implementation returns the hash code of the underlying File
	 * reference.
	 */
	@Override
	public int hashCode() {
		return this.path.hashCode();
	}

}