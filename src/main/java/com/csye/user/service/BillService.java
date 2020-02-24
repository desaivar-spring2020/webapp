package com.csye.user.service;

import com.csye.user.pojo.Bill;
import com.csye.user.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public void deleteBillById(UUID id) {
        billRepository.deleteById(id);
    }

    public Optional<Bill> findById(UUID id){
        return billRepository.findById(id);
    }

    public List<Bill> findingAll(){
        return (List<Bill>) billRepository.findAll();
    }
}