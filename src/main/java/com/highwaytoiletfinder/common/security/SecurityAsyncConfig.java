package com.highwaytoiletfinder.common.security;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class SecurityAsyncConfig {

    @PostConstruct
    public void enableSecurityContextPropagation() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}
