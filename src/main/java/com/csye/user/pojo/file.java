package com.csye.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class file {

    @Id
    @JsonIgnore
    @JsonProperty(value = "fileId")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column
    private String file_name;

    @Column
    private String url;

    @Column
    private String filekey;

    @Column
    private long InstanceLength;

    @Column
    private long ContentLength;

    @Column
    private String Bucketname;

    @Column
    private String Etag;

    @Column
    private Date upload_date;

    @Column
    private long file_size;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(cascade = CascadeType.ALL)
    private Bill bill;

    public file() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getFile_size() {
        return file_size;
    }

    public String getFilekey() {
        return filekey;
    }

    public void setFilekey(String filekey) {
        this.filekey = filekey;
    }

    public long getInstanceLength() {
        return InstanceLength;
    }

    public void setInstanceLength(long instanceLength) {
        InstanceLength = instanceLength;
    }

    public long getContentLength() {
        return ContentLength;
    }

    public void setContentLength(long contentLength) {
        ContentLength = contentLength;
    }

    public String getBucketname() {
        return Bucketname;
    }

    public void setBucketname(String bucketname) {
        Bucketname = bucketname;
    }

    public String getEtag() {
        return Etag;
    }

    public void setEtag(String etag) {
        Etag = etag;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }

}