package com.csye.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Bill {


    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(columnDefinition = "BINARY(16)")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonProperty(value = "id")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column
    private Date createdTs;

    @Column
    private Date updatedTs;

    @Column
    @JsonProperty(value = "vendor")
    private String vender;

    @Column
    @JsonIgnore
    @JsonProperty(value = "authorId")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID authorId;

    @Column
    @JsonProperty(value = "bill_date")
    private String bill_date;

    @Column
    @JsonProperty(value = "due_date")
    private String due_date;

    @Column
    @JsonProperty(value = "amount_due")
    private String amount;

    @ElementCollection
    private List<String> categories = new ArrayList<>();

    @Column
    @JsonProperty(value = "paymentStatus")
    private String paymentstatus;

    public Bill() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Date createdTs) {
        this.createdTs = createdTs;
    }

    public Date getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(Date updatedTs) {
        this.updatedTs = updatedTs;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }
}