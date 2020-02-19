package com.csye.user.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String[] getUserCredentials(String userHeader){

        String[] userHeaderSplit = userHeader.split(" ");
        String decodedString;
        byte[] decodedBytes;
        decodedBytes = Base64.decodeBase64(userHeaderSplit[1]);
        decodedString = new String(decodedBytes);
        String[] userCredentials = decodedString.split(":");
        return userCredentials;
    }

//    public boolean isValidEmail(String email) {
//        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
//        return email.matches(emailPattern);
//    }

    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }
}
