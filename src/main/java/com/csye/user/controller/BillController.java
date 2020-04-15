package com.csye.user.controller;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.csye.user.pojo.Bill;
import com.csye.user.pojo.User;
import com.csye.user.repository.BillRepository;
import com.csye.user.repository.UserRepository;
import com.csye.user.service.BillService;
import com.csye.user.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// stats and logs
import com.csye.user.metrics.StatMetric;
//import com.timgroup.statsd.StatsDClient;
//import com.timgroup.statsd.NonBlockingStatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class BillController {

    // stats and logs
    @Autowired
    private StatMetric statMetric;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private BillRepository billDao;

    //    @Autowired
//    private IRecipeService recipeService;
    @Autowired
    private BillService billService;

    @RequestMapping(value = "/v1/bill", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> createBill(@RequestBody Bill bill, HttpServletRequest req, HttpServletResponse res){

        String[] userCredentials;
        String userName;
        String userHeader;


        // stats and logs
        logger.info("Creating bill");
        statMetric.incrementStat("post.bill");
        // stats and logs - timer
        long now = System.currentTimeMillis();



        if(bill.getVender()==""){
            return new ResponseEntity<Object>("Vendor name shouldn't be null",HttpStatus.BAD_REQUEST);
        }

        if(bill.getBill_date().equals("")){
            return new ResponseEntity<Object>("Cook time and prep time cannot be zero",HttpStatus.BAD_REQUEST);
        }

        if(bill.getDue_date().equals("")){
            return new ResponseEntity<Object>("Recipe serving should be between 1 and 5",HttpStatus.BAD_REQUEST);
        }

        if(bill.getDue_date().compareTo(bill.getBill_date())<0)
        {
            return new ResponseEntity<Object>("Due date is smaller than ....",HttpStatus.BAD_REQUEST);
        }

        if(bill.getAmount()==""){
            return new ResponseEntity<Object>("Title cannot be null or blank",HttpStatus.BAD_REQUEST);
        }

        if(bill.getPaymentstatus()==""){
            return new ResponseEntity<Object>("Cuisine cannot be null or blank",HttpStatus.BAD_REQUEST);
        }

        if(bill.getCategories().size()==0){
            return new ResponseEntity<Object>("No categories provided",HttpStatus.BAD_REQUEST);
        }


        userHeader = req.getHeader("Authorization");

        if(userHeader!=null && userHeader.startsWith("Basic")){
            userCredentials = userService.getUserCredentials(userHeader);
        }
        else{
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        userName = userCredentials[0];

        User existUser = userDao.findByEmailId(userName);

        UUID id = UUID.randomUUID();
        bill.setId(id);
        bill.setCreatedTs(new Date());
        bill.setUpdatedTs(new Date());
        bill.setAuthorId(existUser.getUserId());

        billDao.save(bill);

        return new ResponseEntity<Object>(bill,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/v1/bill/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> getBill(Bill bill, HttpServletRequest req, HttpServletResponse res, @PathVariable("id") UUID id){

        // stats and logs
        logger.info("Getting bill");
        statMetric.incrementStat("get.bill");
        // stats and logs - timer
        long now = System.currentTimeMillis();

        try {
            String userCredentials[];
            String userName;
            String userHeader;
            userHeader = req.getHeader("Authorization");

            userCredentials = userService.getUserCredentials(userHeader);
            userName = userCredentials[0];
            User user = userDao.findByEmailId(userName);



            Optional<Bill> existBill = billService.findById(id);
            if (existBill.isPresent()&& user.getUserId().toString().equals(existBill.get().getAuthorId().toString())) {

                // stats and logs - timer
                long duration = System.currentTimeMillis() - now;
                statMetric.timerStat("get.bill.api.time", duration);

                return new ResponseEntity<Object>(existBill, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception e){
            return new ResponseEntity<Object>("Something went wrong!! Please check your id.",HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/v1/bills", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> getAllBill(Bill bill, HttpServletRequest req, HttpServletResponse res){


        // stats and logs
        logger.info("Getting all bill");
        statMetric.incrementStat("get.all.bill");
        // stats and logs - timer
        long now = System.currentTimeMillis();

        try {

            List<Bill> existBill = billService.findingAll();

            // stats and logs - timer
            long duration = System.currentTimeMillis() - now;
            statMetric.timerStat("get.all.bill.api.time", duration);

            return new ResponseEntity<Object>(existBill, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<Object>("Something went wrong!! Please check your id.",HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/v1/bill/{id}", method=RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> updateRecipe(@RequestBody Bill bill, @PathVariable("id") UUID id, HttpServletRequest req, HttpServletResponse res){

        String userCredentials[];
        String userName;
        String userHeader;
        userHeader = req.getHeader("Authorization");

        // stats and logs
        logger.info("Updating bill");
        statMetric.incrementStat("put.bill");
        // stats and logs - timer
        long now = System.currentTimeMillis();

        userCredentials = userService.getUserCredentials(userHeader);
        userName = userCredentials[0];
        User user = userDao.findByEmailId(userName);

        try {
            Optional<Bill> val = billService.findById(id);
            if (val.isPresent()) {

                if (user.getUserId().toString().equals(val.get().getAuthorId().toString())) {
                    if (bill.getVender() != null) {
                        val.get().setVender(bill.getVender());
                    }
                    if (bill.getBill_date() != null) {
                        val.get().setBill_date(bill.getBill_date());
                    }
                    if (bill.getDue_date() != null) {
                        val.get().setDue_date(bill.getDue_date());
                    }
                    if (bill.getAmount() != null) {
                        val.get().setAmount(bill.getAmount());
                    }
                    if (bill.getCategories().size() != 0) {
                        val.get().setCategories(bill.getCategories());
                    }
                    if (bill.getPaymentstatus() != null) {
                        val.get().setPaymentstatus(bill.getPaymentstatus());
                    }

                    val.get().setCreatedTs(new Date());
                    val.get().setUpdatedTs(new Date());
                    val.get().setAuthorId(user.getUserId());
                    billDao.save(val.get());
                } else {
                    return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }

            // stats and logs - timer
            long duration = System.currentTimeMillis() - now;
            statMetric.timerStat("put.bill.api.time", duration);

            return new ResponseEntity<Object>(val.get(), HttpStatus.OK);
        }
        catch (NullPointerException e){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/v1/bill/{id}", method=RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> deleteRecipe(@PathVariable("id") UUID id, HttpServletRequest req, HttpServletResponse res){
        String userCredentials[];
        String userName;
        String userHeader;
        userHeader = req.getHeader("Authorization");

        // stats and logs
        logger.info("Deleting bill");
        statMetric.incrementStat("delete.bill");
        // stats and logs - timer
        long now = System.currentTimeMillis();

        userCredentials = userService.getUserCredentials(userHeader);
        userName = userCredentials[0];
        User user = userDao.findByEmailId(userName);

        try {
            Optional<Bill> val = billService.findById(id);

            if (val.isPresent()) {
                if (user.getUserId().toString().equals(val.get().getAuthorId().toString())) {
                    billService.deleteBillById(id);

                    // stats and logs - timer
                    long duration = System.currentTimeMillis() - now;
                    statMetric.timerStat("delete.bill.api.time", duration);

                    return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        }
        catch(NullPointerException e){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/v1/bills/due/{x}", method=RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> getBillLinks(@PathVariable("x") int days,HttpServletRequest req, HttpServletResponse res) {

        String userCredentials[];
        String userName;
        String password;
        String userHeader;
        String error;
        String send=null;
        try {
            userHeader = req.getHeader("Authorization");
            userCredentials = userService.getUserCredentials(userHeader);
            userName = userCredentials[0];
            password = userCredentials[1];
            User user = userDao.findByEmailId(userName);

            System.out.println("Totol days"+days);

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {


                final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
                final Map<String, String> attributes = new HashMap<>();
                attributes.put("FifoQueue", "true");
                final CreateQueueRequest createQueueRequest =
                        new CreateQueueRequest("EmailDuesQueue.fifo")
                                .withAttributes(attributes);
                final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

                final SendMessageRequest sendMessageRequest =
                        new SendMessageRequest(myQueueUrl,
                                "Request from user "+user.getUserId().toString());
                sendMessageRequest.setMessageGroupId("msg1");

                final SendMessageResult sendMessageResult = sqs
                        .sendMessage(sendMessageRequest);

                final ReceiveMessageRequest receiveMessageRequest =
                        new ReceiveMessageRequest(myQueueUrl);

                final List<Message> messages = sqs.receiveMessage(receiveMessageRequest)
                        .getMessages();

                Message message= messages.get(0);
                String s= message.getBody();

                String userid = s.substring(s.lastIndexOf(" ")+1);



                List<Bill> billlist= billService.findingAll();
                List<String> list = new ArrayList<>();

                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                String today = formatter.format(new Date());
                LocalDate dateBefore = LocalDate.parse(today);

                for(Bill bill:billlist){
                    if(bill.getAuthorId().toString().equals(user.getUserId().toString())){
                        //  if(bill.getAuthorId().toString().equals(userid)){
                        String dateAfterString = bill.getDue_date();
                        LocalDate dateAfter = LocalDate.parse(dateAfterString);
                        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                        if(noOfDaysBetween<=days&&noOfDaysBetween>0)
                        {
                            list.add(bill.getId().toString());
                        }
                    }
                }
                send = String.join(",", list);
                send=send+","+user.getEmailId();

                AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

                CreateTopicResult topicResult = snsClient.createTopic("SNS");
                String topicArn = topicResult.getTopicArn();
                final PublishRequest publishRequest = new PublishRequest(topicArn, send);

                final PublishResult publishResponse = snsClient.publish(publishRequest);

            }
            else {
                return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
}