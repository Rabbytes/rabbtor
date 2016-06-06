
package com.rabbtor.gsp.config.annotation;

import java.util.List;
import java.util.Set;

public interface WebGspConfigurer
{
    void configureGsp(WebGspSettings config);

    void registerGspTagLibraries(Set<Class<?>> tagLibRegistry);

    void registerTldScanPaths(List<String> scanPaths);

}
