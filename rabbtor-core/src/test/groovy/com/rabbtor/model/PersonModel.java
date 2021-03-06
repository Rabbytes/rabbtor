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


import com.rabbtor.model.annotation.Model;
import com.rabbtor.model.annotation.DisplayName;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Model(value = "person")
public class PersonModel
{
    @DisplayName(value = "name")
    private String name;

    @DisplayName(value = "age")
    private int age;

    @DisplayName(key = "personid")
    private Long id;

    private Map<String,AddressModel> addressMap;

    private AddressModel address;

    private Date dateOfBirth;

    private Set<String> roles;
    private List<AddressModel> addresses;

    private Double[] runs;

    private int privateNum;
    protected int protectedNum;

    protected int getPrivateNum()
    {
        return privateNum;
    }

    @DisplayName(value = "getName")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    @DisplayName(value ="id")
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<String> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<String> roles)
    {
        this.roles = roles;
    }

    public List<AddressModel> getAddresses()
    {
        return addresses;
    }

    public Map<String, AddressModel> getAddressMap()
    {
        return addressMap;
    }

    public void setAddressMap(Map<String, AddressModel> addressMap)
    {
        this.addressMap = addressMap;
    }

    public Double[] getRuns()
    {
        return runs;
    }

    public AddressModel getAddress()
    {
        return address;
    }

    public void setAddress(AddressModel address)
    {
        this.address = address;
    }
}
