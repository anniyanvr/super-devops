// Generated by XCloud DevOps for Codegen, refer: http://dts.devops.wl4g.com

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

package com.wl4g.devops.common.bean.doc;

import com.wl4g.component.core.bean.BaseBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * {@link EnterpriseRepositoryVersion}
 *
 * @author root
 * @version 0.0.1-SNAPSHOT
 * @Date Dec 14, 2020
 * @since v1.0
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class EnterpriseRepositoryVersion extends BaseBean {
    private static final long serialVersionUID = 888336546651166464L;

    /**
     * 
     */
    @NotNull
    private Long repositoryId;

    /**
     * 
     */
    @NotNull
    private String version;

    /**
     * 组织编码
     */
    private String organizationCode;

    public EnterpriseRepositoryVersion() {
    }

    public EnterpriseRepositoryVersion withRepositoryId(Long repositoryId) {
        setRepositoryId(repositoryId);
        return this;
    }

    public EnterpriseRepositoryVersion withVersion(String version) {
        setVersion(version);
        return this;
    }

    public EnterpriseRepositoryVersion withOrganizationCode(String organizationCode) {
        setOrganizationCode(organizationCode);
        return this;
    }
}