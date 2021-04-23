package com.example.a6001cem_artapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainActivityTest {
    @Mock
    MainActivity mainActivity;

    @Before
    public void setUp(){
        mainActivity = Mockito.mock(MainActivity.class);
    }

    @Test
    public void validEmailCheck1() {
        assertTrue(mainActivity.emailValid("e@c.com"));
    }
    @Test
    public void validEmailCheck2(){
        assertTrue(mainActivity.emailValid("e@uni.coventry.ac.uk"));
    }
    @Test
    public void validEmailCheck3(){
        assertTrue(mainActivity.emailValid("e_1@uni.coventry.ac.uk"));
    }
    @Test
    public void validEmailCheck4(){
        assertTrue(mainActivity.emailValid("e_1_e@uni.coventry.ac.uk"));
    }
    @Test
    public void invalidEmailCheck1(){
        assertFalse(mainActivity.emailValid(""));
    }
    @Test
    public void invalidEmailCheck2(){
        assertFalse(mainActivity.emailValid("e"));
    }
    @Test
    public void invalidEmailCheck3(){
        assertFalse(mainActivity.emailValid("e@"));
    }
    @Test
    public void invalidEmailCheck4(){
        assertFalse(mainActivity.emailValid("e@c"));
    }
    @Test
    public void invalidEmailCheck5(){
        assertFalse(mainActivity.emailValid("e.c@c"));
    }

}
