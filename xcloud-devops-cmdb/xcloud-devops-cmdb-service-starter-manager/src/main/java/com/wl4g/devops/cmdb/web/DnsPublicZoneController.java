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
package com.wl4g.devops.cmdb.web;

import com.wl4g.component.common.web.rest.RespBase;
import com.wl4g.component.core.web.BaseController;
import com.wl4g.component.core.bean.model.PageHolder;
import com.wl4g.devops.common.bean.cmdb.DnsPublicZone;
import com.wl4g.devops.cmdb.service.DnsPublicZoneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vjay
 * @date 2019-06-24 14:23:00
 */
@RestController
@RequestMapping("/dnsPublicDomain")
public class DnsPublicZoneController extends BaseController {

	@Autowired
	private DnsPublicZoneService dnsPublicDomainService;

	@RequestMapping(value = "/list")
	public RespBase<?> list(PageHolder<DnsPublicZone> pm, String zone) {
		RespBase<Object> resp = RespBase.create();
		resp.setData(dnsPublicDomainService.page(pm, zone));
		return resp;
	}

	@RequestMapping(value = "/save")
	public RespBase<?> save(@RequestBody DnsPublicZone dnsPublicDomain) {
		RespBase<Object> resp = RespBase.create();
		dnsPublicDomainService.save(dnsPublicDomain);
		return resp;
	}

	@RequestMapping(value = "/detail")
	public RespBase<?> detail(Long id) {
		RespBase<Object> resp = RespBase.create();
		resp.setData(dnsPublicDomainService.detail(id));
		return resp;
	}

	@RequestMapping(value = "/del")
	public RespBase<?> del(Long id) {
		RespBase<Object> resp = RespBase.create();
		dnsPublicDomainService.del(id);
		return resp;
	}

}