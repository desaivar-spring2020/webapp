package com.csye.user.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class User {

    @Id
    @JsonProperty(value = "userId")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID userId;

    @Column
    @JsonProperty(value = "first_name")
    private String firstName;

    @Column
    @JsonProperty(value = "last_name")
    private String lastName;

    @Column
    @JsonProperty(value = "password")
    private String password;

    @Column
    @JsonProperty(value = "email_address")
    private String emailId;

    @Column
    private Date account_created;

    @Column
    private Date account_updated;


    public User() {
    }

    public User(UUID userId,String emailId,String password){
        this.emailId = emailId;
        this.password = password;
        this.userId = userId;
    }


    public Date getAccount_created() {
        return account_created;
    }

    public void setAccount_created(Date account_created) {
        this.account_created = account_created;
    }

    public Date getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(Date account_updated) {
        this.account_updated = account_updated;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                '}';
    }
}

