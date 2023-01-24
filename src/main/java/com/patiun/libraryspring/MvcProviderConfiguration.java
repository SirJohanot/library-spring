package com.patiun.libraryspring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.patiun.libraryspring.mode.mvc")
@ConditionalOnProperty(prefix = "mvc.controller",
        name = "enabled",
        havingValue = "true")
public class MvcProviderConfiguration {
}
