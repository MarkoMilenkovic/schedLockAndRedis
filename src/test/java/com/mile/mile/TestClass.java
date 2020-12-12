package com.mile.mile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class TestClass {

    @Test
    public void test() {
        List<String>  l = new ArrayList<>();
        assertEquals(l.size(), 0);
    }

}
