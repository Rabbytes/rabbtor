/*
 * Copyright 2011 SpringSource.
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
package com.rabbtor.web.taglib;


import com.rabbtor.gsp.GspConfiguration;
import com.rabbtor.taglib.AbstractTemplateVariableBinding;
import com.rabbtor.web.servlet.mvc.RabbtorWebRequest;
import groovy.lang.Binding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Script binding to be used as the top-level binding in GSP evaluation.
 *
 * @author Lari Hotari
 */
public class WebRequestTemplateVariableBinding extends AbstractTemplateVariableBinding
{
    private static Log log = LogFactory.getLog(WebRequestTemplateVariableBinding.class);
    private RabbtorWebRequest webRequest;
    private boolean developmentMode = false;
    private Set<String> requestAttributeVariables = new HashSet<String>();

    private static Map<String, LazyRequestBasedValue> lazyRequestBasedValuesMap = new HashMap<String, LazyRequestBasedValue>();

    static
    {
        Map<String, LazyRequestBasedValue> m = lazyRequestBasedValuesMap;
        m.put("webRequest", webRequest -> webRequest);
        m.put("request", webRequest -> webRequest.getRequest());
        m.put("response", webRequest -> webRequest.getResponse());
        m.put("applicationContext", webRequest -> webRequest.getApplicationContext());
        m.put("session", webRequest -> webRequest.getRequest().getSession());

    }

    public WebRequestTemplateVariableBinding(RabbtorWebRequest webRequest)
    {
        this.webRequest = webRequest;

    }

    public Binding findBindingForVariable(String name)
    {
        Binding binding = super.findBindingForVariable(name);
        if (binding == null)
        {
            if (webRequest.getAttribute(name, RequestAttributes.SCOPE_REQUEST) != null)
            {
                requestAttributeVariables.add(name);
                binding = this;
            }
        }
        if (binding == null && lazyRequestBasedValuesMap.containsKey(name))
        {
            binding = this;
        }
        return binding;
    }

    public boolean isRequestAttributeVariable(String name)
    {
        return requestAttributeVariables.contains(name);
    }

    public boolean isVariableCachingAllowed(String name)
    {
        return !isRequestAttributeVariable(name);
    }

    @Override
    public Object getVariable(String name)
    {
        Object val = getVariablesMap().get(name);
        if (val == null && !getVariablesMap().containsKey(name) && webRequest != null)
        {
            val = webRequest.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
            if (val != null)
            {
                requestAttributeVariables.add(name);
            } else
            {
                LazyRequestBasedValue lazyValue = lazyRequestBasedValuesMap.get(name);
                if (lazyValue != null)
                {
                    val = lazyValue.evaluate(webRequest);
                } else
                {
                    val = resolveMissingVariable(name);
                }

                // warn about missing variables in development mode
                if (val == null && developmentMode)
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("Variable '" + name + "' not found in binding or the value is null.");
                    }
                }
            }
        }
        return val;
    }

    protected Object resolveMissingVariable(String name)
    {
        return null;
    }

    private interface LazyRequestBasedValue
    {
        Object evaluate(RabbtorWebRequest webRequest);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getVariableNames()
    {
        if (getVariablesMap().isEmpty())
        {
            return lazyRequestBasedValuesMap.keySet();
        }

        Set<String> variableNames = new HashSet<String>(lazyRequestBasedValuesMap.keySet());
        variableNames.addAll(getVariablesMap().keySet());
        return variableNames;
    }
}
