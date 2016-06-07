/**
 * Copyright 2016 - Rabbytes Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Rabbytes Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Rabbytes Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Rabbytes Incorporated.
 */
package com.rabbtor.model;


import java.beans.PropertyDescriptor;

public interface ModelPropertyMetadata
{

    boolean isReadOnly();

    boolean isWriteOnly();

    String getPropertyName();

    String getDisplayName();

    String getModelName();

    Class<?> getPropertyType();

    boolean isPrimitive();

    boolean isCollection();

    boolean isMap();

    Class<?> getComponentType();

    ModelMetadata getComponentMetadata();

    ModelMetadata getPropertyTypeMetadata();

    Class<?> getDeclaringClass();

    ModelMetadata getDeclaringModelMetadata();

}
