package com.ecommerce.securitycommon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceTokenProvider {
    private final JwtService jwtService;
    private final String serviceName;
    private String cachedToken;

    public String getToken() {
        if (cachedToken == null) {
            cachedToken = jwtService.generateServiceToken(serviceName, 3_600_000);
        }
        return cachedToken;
    }
}
