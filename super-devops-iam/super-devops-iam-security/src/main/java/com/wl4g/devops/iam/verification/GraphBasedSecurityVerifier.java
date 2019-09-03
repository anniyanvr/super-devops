/*
 * Copyright 2017 ~ 2025 the original author or authors.
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
package com.wl4g.devops.iam.verification;

import com.wl4g.devops.common.exception.iam.VerificationException;
import com.wl4g.devops.iam.common.cache.EnhancedCache;
import com.wl4g.devops.iam.config.IamProperties.MatcherProperties;
import com.wl4g.devops.iam.crypto.keypair.RSACryptographicService;
import com.wl4g.devops.iam.crypto.keypair.RSAKeySpecWrapper;
import com.wl4g.devops.iam.verification.cumulation.Cumulator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wl4g.devops.common.constants.IAMDevOpsConstants.CACHE_FAILFAST_CAPTCHA_COUNTER;
import static com.wl4g.devops.common.constants.IAMDevOpsConstants.CACHE_FAILFAST_MATCH_COUNTER;
import static com.wl4g.devops.iam.common.utils.SessionBindings.bind;
import static com.wl4g.devops.iam.common.utils.SessionBindings.getBindValue;
import static com.wl4g.devops.iam.verification.cumulation.CumulateHolder.newCumulator;
import static com.wl4g.devops.iam.verification.cumulation.CumulateHolder.newSessionCumulator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.shiro.web.util.WebUtils.getCleanParam;

/**
 * Abstract graphic verification code handler
 *
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0
 * @date 2018年12月28日
 * @since
 */
public abstract class GraphBasedSecurityVerifier extends AbstractSecurityVerifier implements InitializingBean {

    /**
     * Apply CAPTCHA image UUID parameter name.
     */
    final public static String DEFAULT_PARAM_APPLY_UUID = "captchaApplyUuid";

    /**
     * Apply UUID expireMs.
     */
    final public static long DEFAULT_APPLY_UUID_EXPIREMS = 60_000;

    /**
     * Apply UUID bit.
     */
    final public static int DEFAULT_APPLY_UUID_BIT = 32;

    /**
     * RSA cryptoGrapic service.
     */
    @Autowired
    protected RSACryptographicService rsaCryptoService;

    /**
     * Matching attempts accumulator
     */
    protected Cumulator matchCumulator;

    /**
     * Apply CAPTCHA attempts accumulator.(Session-based)
     */
    protected Cumulator sessionMatchCumulator;

    /**
     * Apply CAPTCHA attempts accumulator
     */
    protected Cumulator applyCaptchaCumulator;

    /**
     * Apply CAPTCHA attempts accumulator.(Session-based)
     */
    protected Cumulator sessionApplyCaptchaCumulator;

    /**
     * {@link com.google.code.kaptcha.servlet.KaptchaServlet#doGet(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    public Map<String, Object> apply(String owner, @NotNull List<String> factors, @NotNull HttpServletRequest request) {
        // Check limit attempts
        checkApplyAttempts(request, factors);

        // Renew or cleanup CAPTCHA
        reset(owner, true);
        // Check and generate apply UUID.
        // Assert.state(Objects.nonNull(getVerifyCode(true)), "Failed to apply
        // captcha.");

        // Get RSA key.(Used to encrypt sliding X position)
        RSAKeySpecWrapper keySpec = rsaCryptoService.borrow();


        String applyUuid = randomAlphabetic(DEFAULT_APPLY_UUID_BIT);

        bind(applyUuid, keySpec, DEFAULT_APPLY_UUID_EXPIREMS);

        if (log.isDebugEnabled()) {
            log.debug("Apply captcha for applyUuid: {}, secretKey: {}", applyUuid, keySpec);
        }

        return new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put(DEFAULT_PARAM_APPLY_UUID, applyUuid);
            }
        };
    }

    @Override
    public void render(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException {
        String applyUuid = getCleanParam(request, DEFAULT_PARAM_APPLY_UUID);
        Assert.hasText(applyUuid, "Apply graphic captcha uuid is required");
        // Check apply UUID.
        RSAKeySpecWrapper storedKeySpec = getBindValue(applyUuid, false);
        Assert.notNull(storedKeySpec, "Apply graphic captcha uuid has expired.");

        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // Response a JPEG
        response.setContentType("image/jpeg");

        // Create the text for the image and output CAPTCHA image buffer.
        imageWrite(request, response, getVerifyCode(true).getCode());
    }

    @Override
    public boolean isEnabled(@NotNull List<String> factors) {
        Assert.isTrue(!CollectionUtils.isEmpty(factors), "factors must not be empty");
        int enabledCaptchaMaxAttempts = config.getMatcher().getEnabledCaptchaMaxAttempts();

        // Cumulative number of matches based on cache, If the number of
        // failures exceeds the upper limit, verification is enabled
        Long matchCount = matchCumulator.getCumulatives(factors);
        if (log.isInfoEnabled()) {
            log.info("Logon match count: {}, factors: {}", matchCount, factors);
        }
        // Login matching failures exceed the upper limit.
        if (matchCount >= enabledCaptchaMaxAttempts) {
            return true;
        }

        // Cumulative number of matches based on session.
        long sessionMatchCount = sessionMatchCumulator.getCumulatives(factors);
        if (log.isInfoEnabled()) {
            log.info("Logon session match count: {}, factors: {}", sessionMatchCount, factors);
        }

        // Graphic verify-code apply over the upper limit.
        if (sessionMatchCount >= enabledCaptchaMaxAttempts) {
            return true;
        }

        return false;
    }

    @Override
    protected long getVerifyCodeExpireMs() {
        return config.getMatcher().getCaptchaExpireMs();
    }

    @Override
    protected void checkApplyAttempts(@NotNull HttpServletRequest request, @NotNull List<String> factors) {
        int failFastCaptchaMaxAttempts = config.getMatcher().getFailFastCaptchaMaxAttempts();

        // Cumulative number of applications based on caching.
        long applyCaptchaCount = applyCaptchaCumulator.accumulate(factors, 1);
        if (log.isInfoEnabled()) {
            log.info("Check graph verify-code apply, for apply count : {}", applyCaptchaCount);
        }
        if (applyCaptchaCount >= failFastCaptchaMaxAttempts) {
            log.warn("Too many times to apply for graph verify-code, actual: {}, maximum: {}, factors: {}", applyCaptchaCount,
                    failFastCaptchaMaxAttempts, factors);
            throw new VerificationException(bundle.getMessage("GraphBasedVerification.locked"));
        }

        // Cumulative number of applications based on session
        long sessionApplyCaptchaCount = sessionApplyCaptchaCumulator.accumulate(factors, 1);
        if (log.isInfoEnabled()) {
            log.info("Check graph verify-code apply, for session apply count : {}", sessionApplyCaptchaCount);
        }
        // Exceeding the limit
        if (sessionApplyCaptchaCount >= failFastCaptchaMaxAttempts) {
            log.warn("Too many times to apply for session graph verify-code, actual: {}, maximum: {}, factors: {}",
                    sessionApplyCaptchaCount, failFastCaptchaMaxAttempts, factors);
            throw new VerificationException(bundle.getMessage("GraphBasedVerification.locked"));
        }

    }

    /**
     * Write output CAPTCHA buffer image
     *
     * @param request
     * @param response
     * @param storedCode
     * @return
     */
    protected abstract void imageWrite(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                       Object storedCode) throws IOException;

    @Override
    public void afterPropertiesSet() throws Exception {
        MatcherProperties matcher = config.getMatcher();
        // Match accumulator.
        this.matchCumulator = newCumulator(getCache(CACHE_FAILFAST_MATCH_COUNTER), matcher.getFailFastMatchDelay());
        this.sessionMatchCumulator = newSessionCumulator(CACHE_FAILFAST_MATCH_COUNTER, matcher.getFailFastMatchDelay());

        // CAPTCHA accumulator.
        this.applyCaptchaCumulator = newCumulator(getCache(CACHE_FAILFAST_CAPTCHA_COUNTER), matcher.getFailFastCaptchaDelay());
        this.sessionApplyCaptchaCumulator = newSessionCumulator(CACHE_FAILFAST_CAPTCHA_COUNTER,
                matcher.getFailFastCaptchaDelay());

        Assert.notNull(matchCumulator, "matchCumulator is null, please check configure");
        Assert.notNull(sessionMatchCumulator, "sessionMatchCumulator is null, please check configure");
        Assert.notNull(applyCaptchaCumulator, "applyCumulator is null, please check configure");
        Assert.notNull(sessionApplyCaptchaCumulator, "sessionApplyCumulator is null, please check configure");
    }

    /**
     * Get enhanced cache.
     *
     * @param suffix
     * @return
     */
    private EnhancedCache getCache(String suffix) {
        return cacheManager.getEnhancedCache(/*verifyType().name() +*/ suffix);
    }

}