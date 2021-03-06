/*
 * Copyright 2017 ~ 2050 the original author or authors <Wanglsir@gmail.com, 983708408@qq.com>.
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
package com.wl4g.devops.udm.config;

import com.wl4g.devops.udm.service.conversion.*;
import com.wl4g.devops.udm.service.conversion.DocumentConverter.ConverterProviderKind;
import com.wl4g.devops.udm.service.md.FileLocalMdLocator;
import com.wl4g.devops.udm.service.md.MdLocator;
import com.wl4g.devops.udm.service.template.FileLocalGenTemplateLocator;
import com.wl4g.devops.udm.service.template.GenTemplateLocator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Doc auto configuration.
 *
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年5月21日
 * @since
 */
@Configuration
public class DocAutoConfiguration {

	// --- Basic's ---

	@Bean
	@ConfigurationProperties(prefix = "doc")
	public DocProperties docProperties() {
		return new DocProperties();
	}

	// --- Documention conversions. ---

	@Bean
	public DocumentConverter oas3DocumentConverter() {
		return new Oas3DocumentConverter();
	}

	@Bean
	public DocumentConverter swagger2DocumentConverter() {
		return new Swagger2DocumentConverter();
	}

	@Bean
	public DocumentConverter rap2DocumentConverter() {
		return new Rap2DocumentConverter();
	}

	@Bean
	public DocumentConverterAdapter documentConverterAdapter(List<DocumentConverter<ConverterProviderKind>> conversions) {
		return new DocumentConverterAdapter(conversions);
	}

	@Bean
	public GenTemplateLocator genTemplateLocator() {
		return new FileLocalGenTemplateLocator();
	}

	@Bean
	public MdLocator mdLocator() {
		return new FileLocalMdLocator();
	}

}