package com.csye.user.repository;


import com.csye.user.pojo.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends CrudRepository<Image,UUID> {
//    Optional<Recipe> findById(UUID id);
}