package com.sstohnij.stacktraceqabackendv0.config.token;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TokenProps {

    @Value("${stack-trace.secret.token}")
    private String secretKey;

    @Value("${stack-trace.access-token-expiration}")
    private Long accessTokenExpirationInMin;

    @Value("${stack-trace.refresh-token-expiration}")
    private Long refreshTokenExpirationInMin;

    @Value("${stack-trace.confirmation-token-expiration}")
    private Long confirmationTokenExpiration;
}
