package com.csye.user.service;

import com.csye.user.pojo.Image;
import com.csye.user.pojo.Bill;
import com.csye.user.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Optional<Image> findByImageId(UUID id){
        return imageRepository.findById(id);
    }

    public boolean checkIfFileAlreadyExist(Bill bill){
        /*if(bill.getFile()!=null){
            return false;
        }*/
        return true;
    }

    public void deleteFileById(UUID id) {
        imageRepository.deleteById(id);
    }
}