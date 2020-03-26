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
package com.wl4g.devops.iam.config.properties;

import com.wl4g.devops.iam.common.config.AbstractIamProperties.ParamProperties;

/**
 * IAM server parameters configuration properties
 *
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0
 * @date 2018年11月29日
 * @since
 */
public class ServerParamProperties extends ParamProperties {
	private static final long serialVersionUID = 3258460473711285504L;

	/**
	 * Password parameter name at login time of account password.
	 */
	private String credentialName = "credential";

	// --- [Client's secret & signature. ---

	/**
	 * When the client submits the authentication request, it needs to carry the
	 * public key (hex string) generated by itself for the next step
	 * {@link #clientSecretTokenName}
	 * 
	 * @see next-step:{@link #clientSecretTokenName}
	 */
	private String clientSecretName = "clientSecret";

	/**
	 * When the client authentication is successful, the server will respond
	 * encrypted to the {@link #clientSecretTokenName} (using the
	 * {@link #clientSecretName} encryption in the previous step).
	 * 
	 * @see prev-step:{@link #clientSecretName}
	 * @see next-step:{@link #clientSignName}
	 */
	private String clientSecretTokenName = "clientSecretToken";

	/**
	 * The signature string of the client provider, which is calculated based on
	 * the {@link #clientSecretTokenName} returned by the server in the previous
	 * step.
	 * 
	 * @see prev-step:{@link #clientSecretTokenName}
	 */
	private String clientSignName = "clientSign";

	/**
	 * The nonce string of the client provider, which is calculated based on the
	 * {@link #clientSecretTokenName} returned by the server in the previous
	 * step.
	 * 
	 * @see prev-step:{@link #clientSecretTokenName}
	 */
	private String clientNonceName = "clientNoce";

	// --- Client's secret & signature.] ---

	/**
	 * Client type reference parameter name at login time of account password.
	 */
	private String clientRefName = "client_ref";

	/**
	 * Verification verifiedToken parameter name.
	 */
	private String verifiedTokenName = "verifiedToken";

	/**
	 * Dynamic verification code operation action type parameter key-name.
	 */
	private String smsActionName = "action";

	public String getCredentialName() {
		return credentialName;
	}

	public void setCredentialName(String loginPassword) {
		this.credentialName = loginPassword;
	}

	/**
	 * Gets secret public key that the client requests for authentication is
	 * used to login successfully encrypted additional ticket.
	 * 
	 * @return
	 */
	public String getClientSecretName() {
		return clientSecretName;
	}

	public void setClientSecretName(String signatureName) {
		this.clientSecretName = signatureName;
	}

	public String getClientSecretTokenName() {
		return clientSecretTokenName;
	}

	public void setClientSecretTokenName(String clientSecretTokenName) {
		this.clientSecretTokenName = clientSecretTokenName;
	}

	public String getClientSignName() {
		return clientSignName;
	}

	public void setClientSignName(String clientSignName) {
		this.clientSignName = clientSignName;
	}

	public String getClientNonceName() {
		return clientNonceName;
	}

	public void setClientNonceName(String clientNoceName) {
		this.clientNonceName = clientNoceName;
	}

	public String getClientRefName() {
		return clientRefName;
	}

	public void setClientRefName(String clientRefName) {
		this.clientRefName = clientRefName;
	}

	public String getVerifiedTokenName() {
		return verifiedTokenName;
	}

	public void setVerifiedTokenName(String verifiedTokenName) {
		this.verifiedTokenName = verifiedTokenName;
	}

	public String getSmsActionName() {
		return smsActionName;
	}

	public void setSmsActionName(String smsActionName) {
		this.smsActionName = smsActionName;
	}

}