package com.edatasite.workforce.core.config;

import com.antkorwin.xsync.XSync;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XSyncConfig {
    @Bean
    public XSync<String> stringXSync() {
        return new XSync<>();
    }
}
