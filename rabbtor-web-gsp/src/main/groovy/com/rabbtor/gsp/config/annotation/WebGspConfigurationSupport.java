package com.rabbtor.gsp.config.annotation;


import com.rabbtor.gsp.config.TagLibraryRegistry;
import com.rabbtor.gsp.tags.ApplicationTagLib;
import com.rabbtor.gsp.tags.FormatTagLib;
import org.grails.gsp.io.GroovyPageLocator;
import org.grails.gsp.jsp.TagLibraryResolverImpl;
import org.grails.plugins.web.taglib.RenderTagLib;
import org.grails.plugins.web.taglib.SitemeshTagLib;
import org.grails.web.gsp.io.CachingGrailsConventionGroovyPageLocator;
import org.grails.web.gsp.io.GrailsConventionGroovyPageLocator;
import org.grails.web.servlet.view.GrailsLayoutViewResolver;
import org.grails.web.servlet.view.GroovyPageViewResolver;
import org.grails.web.sitemesh.GroovyPageLayoutFinder;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ViewResolver;

import java.util.*;


public abstract class WebGspConfigurationSupport extends GspConfigurationSupport implements EnvironmentAware
{

    @Override
    protected GroovyPageLocator createGroovyPageLocator()
    {
        final List<String> templateRootsCleaned=resolveTemplateRoots();
        CachingGrailsConventionGroovyPageLocator pageLocator = new CachingGrailsConventionGroovyPageLocator() {
            protected List<String> resolveSearchPaths(String uri) {
                List<String> paths=new ArrayList<String>(templateRootsCleaned.size());
                for(String rootPath : templateRootsCleaned) {
                    paths.add(rootPath + cleanUri(uri));
                }
                return paths;
            }

            protected String cleanUri(String uri) {
                uri = StringUtils.cleanPath(uri);
                if(!uri.startsWith("/")) {
                    uri = "/" + uri;
                }
                return uri;
            }
        };
        pageLocator.setReloadEnabled(gspTemplateEngineConfig().gspReloadingEnabled);
        pageLocator.setCacheTimeout(gspTemplateEngineConfig().locatorCacheTimeout);

        return pageLocator;
    }



    @Bean
    @Conditional(GspEnabledCondition.class)
    public GroovyPageLayoutFinder groovyPageLayoutFinder() {
        GroovyPageLayoutFinder groovyPageLayoutFinder = new GroovyPageLayoutFinder();
        groovyPageLayoutFinder.setGspReloadEnabled(gspTemplateEngineConfig().gspReloadingEnabled);
        groovyPageLayoutFinder.setCacheEnabled(gspTemplateEngineConfig().gspLayoutCaching);
        groovyPageLayoutFinder.setEnableNonGspViews(false);
        groovyPageLayoutFinder.setDefaultDecoratorName(gspTemplateEngineConfig().defaultLayoutName);
        return groovyPageLayoutFinder;
    }

    @Bean
    @Conditional(GspEnabledCondition.class)
    public GrailsLayoutViewResolver gspViewResolver() {
        return new GrailsLayoutViewResolver(innerGspViewResolver(), groovyPageLayoutFinder());
    }

    protected ViewResolver innerGspViewResolver() {
        GroovyPageViewResolver innerGspViewResolver = new GroovyPageViewResolver(groovyPagesTemplateEngine(),
                (GrailsConventionGroovyPageLocator) groovyPageLocator());
        innerGspViewResolver.setAllowGrailsViewCaching(!gspTemplateEngineConfig().gspReloadingEnabled || gspTemplateEngineConfig().viewCacheTimeout != 0);
        innerGspViewResolver.setCacheTimeout(gspTemplateEngineConfig().gspReloadingEnabled ? gspTemplateEngineConfig().viewCacheTimeout : -1);
        return innerGspViewResolver;
    }

    @Bean(autowire = Autowire.BY_NAME)
    public TagLibraryResolverImpl jspTagLibraryResolver()
    {

        return new TagLibraryResolverImpl();
    }

    @Override
    public void setEnvironment(Environment environment)
    {
        if (environment instanceof ConfigurableEnvironment)
        {
            ConfigurableEnvironment configEnv = (ConfigurableEnvironment) environment;
            if (!environment.containsProperty("spring.gsp.tldScanPattern")) {
                Properties defaultProperties = createDefaultProperties();
                configEnv.getPropertySources().addLast(new PropertiesPropertySource(GspJspConfiguration.class.getName(), defaultProperties));
            }
        }
    }

    @Override
    protected void registerGrailsProperties(Set<String> grailsProperties)
    {
        super.registerGrailsProperties(grailsProperties);

    }

    protected Properties createDefaultProperties()
    {
        Properties defaultProperties = new Properties();
        // scan for spring JSP taglib tld files by default, also scan for
        defaultProperties.put("spring.gsp.tldScanPattern",
                "classpath*:/META-INF/spring*.tld,classpath*:/META-INF/fmt.tld,classpath*:/META-INF/c.tld,classpath*:/META-INF/rabbtor*.tld,classpath*:/META-INF/c-1_0-rt.tld");
        return defaultProperties;
    }

    @Bean
    TagLibraryRegistry gspTagLibraryRegistry() {
        TagLibraryRegistry registry = new TagLibraryRegistry();
        Set<Class<?>> instances = new HashSet<>();
        instances.addAll(Arrays.asList(new Class<?>[] {ApplicationTagLib.class,
                RenderTagLib.class, SitemeshTagLib.class, FormatTagLib.class}));
        registerTagLibClasses(instances);
        registry.setTagLibInstances(Arrays.asList(instances.toArray()));
        return registry;
    }

    protected void registerTagLibClasses(Set<Class<?>> instances)
    {

    }
}