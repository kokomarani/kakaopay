package com.kakaopay.project.Utils;

import org.apache.commons.lang3.RandomUtils;

public class CalculateUtils {
    public static long[] divideMoney(long amount, int count) {
        long[] array = new long[count];
        long max = amount / count;
        long min = amount / count / count;

        for (int i = 0; i < count - 1; i++) {
            array[i] = RandomUtils.nextLong(min, max);
            amount -= array[i];
        }
        array[count - 1] = amount;
        return array;
    }
}
