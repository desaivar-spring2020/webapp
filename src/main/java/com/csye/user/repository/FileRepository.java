package com.csye.user.repository;


import com.csye.user.pojo.file;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends CrudRepository<file,UUID> {
//    Optional<Recipe> findById(UUID id);
}