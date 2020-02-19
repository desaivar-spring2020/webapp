package com.csye.user.service;

import com.csye.user.pojo.file;
import com.csye.user.pojo.Bill;
import com.csye.user.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public Optional<file> findByImageId(UUID id){
        return fileRepository.findById(id);
    }

    public boolean checkIfFileAlreadyExist(Bill bill){
        /*if(bill.getFile()!=null){
            return false;
        }*/
        return true;
    }

    public void deleteFileById(UUID id) {
        fileRepository.deleteById(id);
    }
}