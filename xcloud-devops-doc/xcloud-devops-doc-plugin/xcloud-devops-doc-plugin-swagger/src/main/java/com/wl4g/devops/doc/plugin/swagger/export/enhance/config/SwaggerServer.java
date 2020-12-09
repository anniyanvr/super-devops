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
package com.wl4g.devops.doc.plugin.swagger.export.enhance.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariables;

import java.util.Collections;
import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;

public class SwaggerServer {

	/**
	 * REQUIRED. A URL to the target host. This URL supports Server Variables
	 * and MAY be relative, to indicate that the host location is relative to
	 * the location where the OpenAPI document is being served. Variable
	 * substitutions will be made when a variable is named in {brackets}.
	 */
	@Parameter(required = true)
	private String url;

	/**
	 * An optional string describing the host designated by the URL. CommonMark
	 * syntax MAY be used for rich text representation.
	 */
	@Parameter
	private String description;

	/**
	 * A map between a variable name and its value. The value is used for
	 * substitution in the server's URL template.
	 */
	@Parameter
	private Map<String, SwaggerServerVariable> variables = Collections.emptyMap();

	@Parameter
	private Map<String, Object> extensions = Collections.emptyMap();

	public Server createServerModel() {
		Server server = new Server();
		server.setUrl(url);
		server.setDescription(description);

		if (variables != null && !variables.isEmpty()) {
			ServerVariables vs = new ServerVariables();
			variables.entrySet().forEach(v -> vs.addServerVariable(v.getKey(), v.getValue().createServerVariableModel()));
			server.setVariables(vs);
		}

		server.setExtensions(extensions);

		return server;
	}

}