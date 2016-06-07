/*
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
package com.rabbtor.validation

import spock.lang.Specification

import javax.validation.Valid
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.NotNull

class HibernateValidatorSpec extends Specification
{
    ValidatorFactory factory;
    Validator validator;

    def setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    def 'test get constraints'() {
        when:
        def bean = validator.getConstraintsForClass(Person)
        then:
        bean.isBeanConstrained()


    }


    public static class Person {
        @NotNull
        Long id

        @NotNull
        @Valid
        List<Address> names;
    }

    public static class Address {

        @NotNull
        String zipCode

    }

}
