package com.csye.user.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClient {
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Value("{amazonProperties.region}")
    private String region;

    @PostConstruct
    private void initializeAmazon() {
        //System.out.println(this.accessKey);
        //System.out.println(this.secretKey);
        BasicAWSCredentials creds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3client = AmazonS3Client.builder()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();
//        System.out.println(this.accessKey);
//        System.out.println(this.secretKey);
        /*BasicAWSCredentials creds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3client = AmazonS3Client.builder()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();*/
//        InstanceProfileCredentialsProvider provider
//                = new InstanceProfileCredentialsProvider(true);
//        s3client = AmazonS3Client.builder()
//                .withRegion("us-east-1")
//                .withCredentials(provider)
//                .build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

//    public void getMetadata(File fileName){
//        System.out.println(s3client.getObjectMetadata(bucketName,fileName.toString()));
//    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
        //.withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "", fileName));
        return "Successfully deleted";
    }

    public S3Object getFile(String fileUrl){
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        return s3client.getObject(new GetObjectRequest(bucketName + "", fileName));
    }
}