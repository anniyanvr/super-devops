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
package com.wl4g.devops.udc.codegen.engine.resolver.db;

import static com.wl4g.component.common.lang.Assert2.hasTextOf;

import com.wl4g.devops.udc.codegen.bean.GenDataSource;

/**
 * {@link OracleV11gMetadataResolver}
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020-09-11
 * @since
 */
public class OracleV11gMetadataResolver extends AbstractDbMetadataResolver {

	/**
	 * New {@link OracleV11gMetadataResolver}
	 * 
	 * @param genDS
	 */
	public OracleV11gMetadataResolver(GenDataSource genDS) {
		this("jdbc:oracle:thin@//".concat(hasTextOf(genDS.getHost(), "dbHost")).concat(":")
				.concat(hasTextOf(genDS.getPort(), "dbPort")).concat("/").concat(hasTextOf(genDS.getDatabase(), "dbName")),
				genDS.getUsername(), genDS.getPassword());
	}

	protected OracleV11gMetadataResolver(String url, String username, String password) {
		super("oracle.jdbc.OracleDriver", url, username, password);
	}

}