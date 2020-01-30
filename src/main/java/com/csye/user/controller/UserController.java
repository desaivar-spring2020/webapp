package com.csye.user.controller;

import com.csye.user.pojo.User;
import com.csye.user.repository.UserRepository;
import com.csye.user.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private UserService userService;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    String userHeader;

    @RequestMapping(value = "/v1/user/self", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> userHome(HttpServletRequest req, HttpServletResponse res) {

        String[] userCredentials;
        String userName;
        String password;
        userHeader = req.getHeader("Authorization");

        //user sending no userName and password
        if(userHeader.endsWith("Og==")) {
            return new ResponseEntity<Object>("No Credentials sent",HttpStatus.BAD_REQUEST);
        }
        else if (userHeader!=null && userHeader.startsWith("Basic")) {
            userCredentials = userService.getUserCredentials(userHeader);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);

        }

        userName = userCredentials[0];
        password = userCredentials[1];

        User existUser = userDao.findByEmailId(userName);
        HashMap<String,String> userDetails = new HashMap<>();

        if(existUser!=null && BCrypt.checkpw(password, existUser.getPassword())){
            userDetails.put("id",existUser.getUserId().toString());
            userDetails.put("firstName",existUser.getFirstName());
            userDetails.put("lastName",existUser.getLastName());
            userDetails.put("emailId",existUser.getEmailId());
            userDetails.put("account_created",existUser.getAccount_created().toString());
            userDetails.put("account_updated",existUser.getAccount_updated().toString());

        }
        else{
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<Object>(userDetails, HttpStatus.OK);

    }


    @RequestMapping(value = "/v1/user", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> createUser(@RequestBody User user, HttpServletRequest req, HttpServletResponse res){

        //if user already exist
        User existUser = userDao.findByEmailId(user.getEmailId());
        if(existUser!=null){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        //check user has sent all fields
        if(user.getPassword()==null || user.getFirstName()==null || user.getLastName()==null ||
            user.getEmailId()==null)
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);

//        if(!userService.isValidEmail(user.getEmailId())){
//            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
//        }

        if(!EmailValidator.getInstance().isValid(user.getEmailId())){
            return new ResponseEntity<Object>("3",HttpStatus.BAD_REQUEST);
        }

        if(!userService.isValidPassword(user.getPassword())){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        Date d= new Date();

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        UUID id = UUID.randomUUID();
        user.setUserId(id);
        user.setAccount_created(new Date());
        user.setAccount_updated(new Date());
        userDao.save(user);

        HashMap<String,String> userDetails = new HashMap<>();

        userDetails.put("id",user.getUserId().toString());
        userDetails.put("firstName",user.getFirstName());
        userDetails.put("lastName",user.getLastName());
        userDetails.put("emailId",user.getEmailId());
        userDetails.put("account_created",new Date().toString());
        userDetails.put("account_updated",new Date().toString());

        return new ResponseEntity<Object>(userDetails,HttpStatus.CREATED);
    }

    @RequestMapping(value="/v1/user/self", method=RequestMethod.PUT,produces="application/json")
    @ResponseBody
    public ResponseEntity<Object> updateUser(@RequestBody User user,HttpServletRequest req,HttpServletResponse res){

        //checking if user sent no data to update
        if(user.equals(null)){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        //user not allowed to update certain attributes
        if(user.getEmailId()!=null || user.getUserId()!=null){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        //to store user credentials
        String[] userCredentials;
        String userName;
        String password;

        //variables to store update values from user
        String updateFirstName, updateLastName, passwordUpdate;
        userHeader = req.getHeader("Authorization");

        //no credentials provided
        if(userHeader.endsWith("Og==")) {
            return new ResponseEntity<Object>("No Credentials sent",HttpStatus.BAD_REQUEST);
        }
        else if (userHeader!=null && userHeader.startsWith("Basic")) {
            userCredentials = userService.getUserCredentials(userHeader);
        }
        else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        userName = userCredentials[0];
        password = userCredentials[1];

        User existUser = userDao.findByEmailId(userName);
        updateFirstName = user.getFirstName();
        updateLastName = user.getLastName();
        passwordUpdate = user.getPassword();
        System.out.println("updateLastName: "+updateLastName);
//        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));
//        System.out.println(BCrypt.checkpw(password, BCrypt.hashpw(password, BCrypt.gensalt())));

        if(existUser!=null && BCrypt.checkpw(password, existUser.getPassword())){
            if(updateFirstName!=null){
                existUser.setFirstName(updateFirstName);
            }
            if(updateLastName!=null){
                System.out.println("lastName not null");
                existUser.setLastName(updateLastName);
            }
            if(passwordUpdate!=null){
                existUser.setPassword(BCrypt.hashpw(passwordUpdate, BCrypt.gensalt()));
            }
            existUser.setAccount_updated(new Date());
            userDao.save(existUser);
        }
        else{
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);

    }

    

}
