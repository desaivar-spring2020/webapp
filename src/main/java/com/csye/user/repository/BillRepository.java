package com.csye.user.repository;

import com.csye.user.pojo.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillRepository extends CrudRepository<Bill,UUID> {
//    Optional<Recipe> findById(UUID id);
}