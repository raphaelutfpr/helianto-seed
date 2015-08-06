/*
 * Copyright 2010 the original author or authors.
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
package org.helianto.home.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

import org.springframework.web.multipart.MultipartFile;

/**
 * Loadable DTO.
 * 
 * @author mauriciofernandesdecastro
 */
public final class UploadableData {
	
	private String name;
	private final byte[] bytes;
	private String contentType;

	/**
	 * Constructor.
	 * 
	 * @param multipartFile
	 * @throws IOException
	 */
	public UploadableData(MultipartFile multipartFile) throws IOException {
		this(multipartFile.getName(), multipartFile.getBytes());
		String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(multipartFile.getBytes()));
		if (contentType==null) {
			contentType = multipartFile.getContentType();
		}
		setContentType(contentType);
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param bytes
	 */
	public UploadableData(String name, byte[] bytes) {
		this.name = name;
		this.bytes = bytes;
	}

	/**
	 * Construtor.
	 * 
	 * @param name
	 * @param bytes
	 * @param contentType
	 */
	public UploadableData(String name, byte[] bytes, String contentType) {
		this(name, bytes);
		setContentType(contentType);
	}

	/**
	 * The name of the file.
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The file data as a byte array.
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * The file content type.
	 */
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
		
}