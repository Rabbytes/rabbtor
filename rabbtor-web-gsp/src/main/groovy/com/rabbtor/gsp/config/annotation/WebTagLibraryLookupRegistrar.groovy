package com.rabbtor.gsp.config.annotation

import com.rabbtor.gsp.tags.ApplicationTagLib
import com.rabbtor.gsp.tags.FormatTagLib
import grails.gsp.TagLib
import groovy.transform.CompileStatic
import org.grails.plugins.web.taglib.RenderTagLib
import org.grails.plugins.web.taglib.SitemeshTagLib
import org.grails.web.pages.StandaloneTagLibraryLookup
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

@CompileStatic
public class WebTagLibraryLookupRegistrar extends TagLibraryLookupRegistrar
{



}
