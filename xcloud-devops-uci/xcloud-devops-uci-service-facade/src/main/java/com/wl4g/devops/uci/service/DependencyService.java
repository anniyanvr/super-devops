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
package com.wl4g.devops.uci.service;

import com.wl4g.component.rpc.feign.core.annotation.FeignConsumer;
import com.wl4g.devops.common.bean.uci.Dependency;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author vjay
 * @date 2019-05-22 11:33:00
 */
@FeignConsumer(name = "${provider.serviceId.uci-facade:uci-facade}")
@RequestMapping("/dependency-service")
public interface DependencyService {

	@RequestMapping(value = "/getHierarchyDependencys", method = POST)
	LinkedHashSet<Dependency> getHierarchyDependencys(@RequestParam("projectId") Long projectId, @RequestBody LinkedHashSet<Dependency> set);

}