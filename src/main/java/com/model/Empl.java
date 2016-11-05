package com.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Artemie on 09.10.2016.
 */
@XmlRootElement(name = "empl")
@XmlAccessorType(XmlAccessType.FIELD)
public class Empl {
    private Integer id;
    private String firstname;
    private String lastname;
    private Integer age;
    private Integer salary;
    private Long createdDate;

    public Empl(){}

    public Empl(Integer id, String firstname, String lastname, Integer age, Integer salary, Long createdDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.salary = salary;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "com.model.Empl ( id="+id+", firstname="+firstname+", lastname"+lastname+", age="+age+", salary="+salary+", createdDate="+createdDate+");";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}