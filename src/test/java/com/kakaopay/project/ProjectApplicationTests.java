package com.kakaopay.project;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectApplicationTests {

    @Test
    void contextLoads() {
        long[] array = new long[2];
        long amount = 90;
        int count = 2;
        long max = amount / count;
        long min = amount / count / count;
        for (int i = 0; i < count - 1; i++) {

            array[i] = RandomUtils.nextLong(min, max);
            amount -= array[i];
        }
        array[count - 1] = amount;



        for(int i=0;i<array.length;i++){
            System.out.println(array[i]);
        }
    }



}
