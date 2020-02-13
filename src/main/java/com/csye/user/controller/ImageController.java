package com.csye.user.controller;

import com.csye.user.pojo.Bill;
import com.csye.user.pojo.Image;
import com.csye.user.pojo.User;
import com.csye.user.repository.BillRepository;
import com.csye.user.repository.ImageRepository;
import com.csye.user.repository.UserRepository;
import com.csye.user.service.BillService;
import com.csye.user.service.ImageService;
import com.csye.user.service.UserService;
//import com.google.common.primitives.Longs;
//import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import javax.imageio.ImageIO;

@RestController
public class ImageController {

    String userHeader;

    @Autowired
    UserService userService;

    @Autowired
    private BillService billService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BillRepository billRepository;

    @RequestMapping(value = "/v1/bill/{id}/file", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> uploadImage(@RequestPart(value = "file") MultipartFile file, HttpServletRequest req, HttpServletResponse res, @PathVariable("id") UUID id) {
        String[] userCredentials;
        String userName;
        String password;
        String userHeader;
        JSONObject jo = null;
        String error;
        String imgURL;
        String imgId;

        //check if user uploaded an image file only
        try (InputStream input = file.getInputStream()) {

            if (ImageIO.read(input).toString() == null) {
                error = "{\"error\": \"Input file is not an image\"}";
                jo = new JSONObject(error);
                return new ResponseEntity<Object>(jo.toString(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            error = "{\"error\": \"Invalid input\"}";
            try {
                jo = new JSONObject(error);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return new ResponseEntity<Object>(jo.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            userHeader = req.getHeader("Authorization");

            if (userHeader != null && userHeader.startsWith("Basic")) {
                userCredentials = userService.getUserCredentials(userHeader);
            } else {
                System.out.println("ONE");
                error = "{\"error\": \"Please give Basic auth as authorization1!!\"}";
                jo = new JSONObject(error);
                return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
            }

            userName = userCredentials[0];
            password = userCredentials[1];

            User existUser = userDao.findByEmailId(userName);
            if (existUser != null && BCrypt.checkpw(password, existUser.getPassword())) {
                //check if recipe exists for the id given
                Bill existBill = billService.findById(id).get();
                if (existBill != null) {
                    //checking if userId matches author Id in recipe
                    if (!(existUser.getUserId().toString().equals(existBill.getAuthorId().toString()))) {
                        error = "{\"error\": \"User unauthorized to add file to this Bill!!\"}";
                        jo = new JSONObject(error);
                        return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
                    }
                    //check if recipe already has an image
                    if (imageService.checkIfFileAlreadyExist(existBill)) {
                        Image f = new Image();

                        UUID imageId = UUID.randomUUID();
                        f.setId(imageId);
                        //f.setUrl(file.getResource().getURL().toString());
                        f.setFile_name(file.getResource().getFilename());
                        f.setUpload_date(new Date());
                        f.setBill(existBill);
                        System.out.println(file.getBytes());
                        //f.setFile(file.getBytes());
                        //saving to local db
                        imageRepository.save(f);
                        //setting recipe object

                        HashMap<String, String> imageDetails = new HashMap<>();
                        imageDetails.put("Image ID", f.getId().toString());
                        imageDetails.put("Image URL", f.getUrl());
                        return new ResponseEntity<Object>(imageDetails, HttpStatus.CREATED);
                    } else {
                        error = "{\"error\": \"Image for Bill already exists\"}";
                        jo = new JSONObject(error);
                        return new ResponseEntity<Object>(jo.toString(), HttpStatus.BAD_REQUEST);
                    }

                } else {
                    error = "{\"error\": \"BillId not found\"}";
                    jo = new JSONObject(error);
                    return new ResponseEntity<Object>(jo.toString(), HttpStatus.NOT_FOUND);
                }
                //return new ResponseEntity<Object>(recipe,HttpStatus.CREATED);
            } else {
                error = "{\"error\": \"User unauthorized to add Image to this Bill!!\"}";
                jo = new JSONObject(error);
                return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("TWO");
            error = "{\"error\": \"Please provide basic auth as authorization3!!\"}";
            try {
                jo = new JSONObject(error);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/v1/bill/{billId}/file/{fileId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> getFile(HttpServletRequest req, HttpServletResponse res, @PathVariable("billId") UUID billId, @PathVariable("fileId") UUID fileId) throws JSONException {

        JSONObject jo;
        String error;
        try {
            Optional<Bill> existBill = billService.findById(billId);
            if (existBill.isPresent()) {
                Optional<Image> f = imageService.findByImageId(fileId);
                if (f.get() != null) {
                    HashMap<String, String> fileDetails = new HashMap<>();
                    fileDetails.put("Image ID", f.get().getId().toString());
                    fileDetails.put("Image URL", f.get().getUrl());
                    return new ResponseEntity<Object>(fileDetails, HttpStatus.OK);
                } else {
                    error = "{\"error\": \"FileId not found\"}";
                    jo = new JSONObject(error);
                    return new ResponseEntity<Object>(jo.toString(), HttpStatus.NOT_FOUND);
                }
            } else {
                error = "{\"error\": \"Bill Id not found\"}";
                jo = new JSONObject(error);
                return new ResponseEntity<Object>(jo.toString(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            error = "{\"error\": \"Something went wrong!! Please check your id.\"}";
            jo = new JSONObject(error);
            return new ResponseEntity<Object>(jo.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/v1/bill/{billId}/file/{fileId}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> deleteFile(HttpServletRequest req, HttpServletResponse res,
                                             @PathVariable("billId") UUID billId, @PathVariable("fileId") UUID fileId) throws JSONException {

        String[] userCredentials;
        String userName;
        String password;
        String userHeader;
        JSONObject jo;
        String error;
        try {
            userHeader = req.getHeader("Authorization");

            if (userHeader != null && userHeader.startsWith("Basic")) {
                userCredentials = userService.getUserCredentials(userHeader);
            } else {
                error = "{\"error\": \"Please give Basic auth as authorization!!\"}";
                jo = new JSONObject(error);
                return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
            }

            userName = userCredentials[0];
            password = userCredentials[1];

            User existUser = userDao.findByEmailId(userName);
            if (existUser != null && BCrypt.checkpw(password, existUser.getPassword())) {
                //check if bill exists for the id given
                Optional<Bill> existBill = billService.findById(billId);
                if (existBill.isPresent()) {
                    //checking if userId matches author Id in recipe
                    if (!(existUser.getUserId().toString().equals(existBill.get().getAuthorId().toString()))) {
                        error = "{\"error\": \"User unauthorized to delete file to this file!!\"}";
                        jo = new JSONObject(error);
                        return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
                    }

                    //check if bill already has an file
                    Image f = imageService.findByImageId(fileId).get();
                    if (f != null) {
                        List<Image> imageList = existBill.get().getImages();
                        for (Image img : imageList) {
                            if (img.getId().toString().equals(f.getId().toString())) {
                                imageList.remove(img);
                                existBill.get().setImages(imageList);
                                error = "{\"Msg\": \"Image Deleted Successfully\"}";
                                jo = new JSONObject(error);
                                return new ResponseEntity<Object>(jo.toString(), HttpStatus.OK);
                            }

                        }
                    } else {
                        error = "{\"error\": \"Image for bill doesn't exist\"}";
                        jo = new JSONObject(error);
                        return new ResponseEntity<Object>(jo.toString(), HttpStatus.OK);
                    }

                } else {
                    error = "{\"error\": \"BillId not found\"}";
                    jo = new JSONObject(error);
                    return new ResponseEntity<Object>(jo.toString(), HttpStatus.NOT_FOUND);
                }
                //return new ResponseEntity<Object>(recipe,HttpStatus.CREATED);
            } else {
                error = "{\"error\": \"User unauthorized to add file to this bill!!\"}";
                jo = new JSONObject(error);
                return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            System.out.println("TWO");
            System.out.println(e);
            error = "{\"error\": \"Please provide basic auth as authorization!!\"}";
            jo = new JSONObject(error);
            return new ResponseEntity<Object>(jo.toString(), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
}


