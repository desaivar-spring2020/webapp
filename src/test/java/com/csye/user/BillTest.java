package com.csye.user;

import com.csye.user.pojo.Bill;
import com.csye.user.repository.BillRepository;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

public class BillTest {

    @Mock
    BillRepository recipeDao = Mockito.mock(BillRepository.class);

    @Test
    public void testAddRecipe(){



        UUID id = UUID.randomUUID();
        Date d = new Date();
        Bill bill = new Bill();

        bill.setId(id);
        bill.setCreatedTs(d);
        bill.setUpdatedTs(d);
        bill.setAuthorId(id);
        bill.getCategories().add("test");

        recipeDao.save(bill);

        Mockito.verify(recipeDao,Mockito.times(1)).save(bill);


    }
}