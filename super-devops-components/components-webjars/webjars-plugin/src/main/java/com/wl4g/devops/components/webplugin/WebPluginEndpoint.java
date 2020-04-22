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
package com.wl4g.devops.components.webplugin;

import static com.google.common.base.Charsets.UTF_8;
import static com.wl4g.devops.tool.common.serialize.JacksonUtils.toJSONString;
import static com.wl4g.devops.tool.common.web.WebUtils2.getRequestParam;
import static java.util.Locale.US;
import static org.apache.commons.lang3.StringUtils.endsWithAny;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.wl4g.devops.common.web.embedded.GenericEmbeddedWebappsEndpoint;
import com.wl4g.devops.components.webplugin.config.WebPluginProperties;
import com.wl4g.devops.components.webplugin.handler.WebPluginHandler;

/**
 * {@link WebPluginEndpoint}
 * 
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020年3月31日
 * @since
 */
public class WebPluginEndpoint extends GenericEmbeddedWebappsEndpoint {

	@Autowired
	protected WebPluginHandler pluginHandler;

	public WebPluginEndpoint(WebPluginProperties config) {
		super(config);
	}

	@Override
	protected byte[] decorateResource(String filepath, HttpServletRequest request, byte[] fileBuf) {
		if (endsWithAny(filepath.toLowerCase(US), JS_BOOTSTRAP, JS_BOOTSTRAP_MIN)) {
			String pluginName = getRequestParam(request, "p", true);
			log.info("Loading bootstrap.js ... p: {}", filepath);
			String content = new String(fileBuf, UTF_8);
			content = content.replace(VAR_PLUGIN_DEPENDENCIES, toJSONString(pluginHandler.getPlugin(pluginName)));
			return content.getBytes(UTF_8);
		}
		return fileBuf;
	}

	final public static String JS_BOOTSTRAP = "bootstrap.js";
	final public static String JS_BOOTSTRAP_MIN = "bootstrap.min.js";
	final public static String VAR_PLUGIN_DEPENDENCIES = "\"${{pluginInfo}}\"";

}