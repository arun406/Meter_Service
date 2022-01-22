package com.aktimetrics.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilTest {

    @Test
    void getLocalDateTime() {

    }

    @Test
    void testGetLocalDateTime() {

        final String original = "2021-06-25 12:35";
        final LocalDateTime localDateTime = DateTimeUtil.getLocalDateTime(original, "yyyy-MM-dd HH:mm");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Format LocalDateTime
        String expected = localDateTime.format(formatter);

        Assertions.assertEquals(original, expected);

    }

    @Test
    void testGetLocalDateTime1() {
    }
}