package com.patiun.libraryspring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.patiun.libraryspring.mode.rest")
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "false",
        matchIfMissing = true)
public class RestProviderConfiguration {
}
