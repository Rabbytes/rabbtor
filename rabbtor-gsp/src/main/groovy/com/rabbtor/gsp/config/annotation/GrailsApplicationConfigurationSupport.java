
package com.rabbtor.gsp.config.annotation;


import grails.core.GrailsApplication;
import grails.core.StandaloneGrailsApplication;
import org.grails.config.PropertySourcesConfig;
import org.grails.encoder.CodecLookup;
import org.grails.encoder.impl.StandaloneCodecLookup;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class GrailsApplicationConfigurationSupport implements EnvironmentAware
{
    Environment environment;

    public Environment getEnvironment()
    {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }

    @Bean
    SpringBootGrailsApplication grailsApplication() {

        SpringBootGrailsApplication grailsApplication = new SpringBootGrailsApplication();
        configureGrailsApplication(grailsApplication);
        return grailsApplication;
    }

    protected void configureGrailsApplication(SpringBootGrailsApplication grailsApplication)
    {
        copyEnvironmentProperties(grailsApplication);
    }

    private void copyEnvironmentProperties(SpringBootGrailsApplication grailsApplication)
    {

        if (environment != null) {
            Map<String,Object> flatConfig = grailsApplication.getFlatConfig();
            Set<String> grailsProperties = new HashSet();
            registerGrailsProperties(grailsProperties);
            for (String property : grailsProperties) {
                if (environment.containsProperty(property)) {
                    flatConfig.put(property,environment.getProperty(property));
                }
            }
        }
    }

    protected void registerGrailsProperties(Set<String> grailsProperties)
    {

    }

    @Bean
    CodecLookup codecLookup() {
        return new StandaloneCodecLookup();
    }

    /**
     * Makes Spring Boot application properties available in the GrailsApplication instance's flatConfig
     *
     */
    public static class SpringBootGrailsApplication extends StandaloneGrailsApplication implements EnvironmentAware
    {
        private Environment environment;

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void updateFlatConfig() {
            super.updateFlatConfig();
            if(this.environment instanceof ConfigurableEnvironment) {
                ConfigurableEnvironment configurableEnv = ((ConfigurableEnvironment)environment);
                for(PropertySource<?> propertySource : configurableEnv.getPropertySources()) {
                    if(propertySource instanceof EnumerablePropertySource) {
                        EnumerablePropertySource<?> enumerablePropertySource = (EnumerablePropertySource)propertySource;
                        for(String propertyName : enumerablePropertySource.getPropertyNames()) {
                            flatConfig.put(propertyName, enumerablePropertySource.getProperty(propertyName));
                        }
                    }
                }
            }
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
            updateFlatConfig();
        }

        public Environment getEnvironment()
        {
            return environment;
        }
    }
}
