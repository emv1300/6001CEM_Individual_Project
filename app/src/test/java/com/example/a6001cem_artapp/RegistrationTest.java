package com.example.a6001cem_artapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegistrationTest {

    @Mock
    Registration registerActivity;


    @Before
    public void setUp(){
        registerActivity = Mockito.mock(Registration.class);

    }

    @Test
    public void validEmailCheck1() {
        assertTrue(Registration.emailValid("e@c.com"));
    }
    @Test
    public void validEmailCheck2(){
        assertTrue(Registration.emailValid("e@uni.coventry.ac.uk"));
    }
    @Test
    public void validEmailCheck3(){
        assertTrue(Registration.emailValid("e_1@uni.coventry.ac.uk"));
    }
    @Test
    public void validEmailCheck4(){
        assertTrue(Registration.emailValid("e_1_e@uni.coventry.ac.uk"));
    }
    @Test
    public void invalidEmailCheck1(){
        assertFalse(Registration.emailValid(""));
    }
    @Test
    public void invalidEmailCheck2(){
        assertFalse(Registration.emailValid("e"));
    }
    @Test
    public void invalidEmailCheck3(){
        assertFalse(Registration.emailValid("e@"));
    }
    @Test
    public void invalidEmailCheck4(){
        assertFalse(Registration.emailValid("e@c"));
    }
    @Test
    public void invalidEmailCheck5(){
        assertFalse(Registration.emailValid("e.c@c"));
    }
    @Test
    public void invalidRepeatingCharsCheck1(){
        assertFalse(Registration.checkString(""));
    }
    @Test
    public void invalidRepeatingCharsCheck2(){
        assertFalse(Registration.checkString("aaa"));
    }
    @Test
    public void invalidRepeatingCharsCheck3(){
        assertFalse(Registration.checkString("Usernnname"));
    }
    @Test
    public void validRepeatingCharsCheck1(){
        assertTrue(Registration.checkString("Uuuusername"));
    }
    @Test
    public void validRepeatingCharsCheck2(){
        assertTrue(Registration.checkString("uuuu"));
    }
    @Test
    public void validPasswordCheck1(){
        assertTrue(Registration.passwordCheck("aA1!pass"));
    }

    @Test
    public void validPasswordCheck2(){
        assertTrue(Registration.passwordCheck("PaSSword123!@*"));
    }

    @Test
    public void invalidPasswordCheck1(){
        assertFalse(Registration.passwordCheck(""));
    }
    @Test
    public void invalidPasswordCheck2(){
        assertFalse(Registration.passwordCheck("pass"));
    }
    @Test
    public void invalidPasswordCheck3(){
        assertFalse(Registration.passwordCheck("password"));
    }
    @Test
    public void invalidPasswordCheck4(){
        assertFalse(Registration.passwordCheck("password1!"));
    }
    @Test
    public void invalidPasswordCheck5(){
        assertFalse(Registration.passwordCheck("PASSWORD1!"));
    }
    @Test
    public void invalidPasswordCheck6(){
        assertFalse(Registration.passwordCheck("PASSWORD!!"));
    }
    @Test
    public void invalidPasswordCheck7(){
        assertFalse(Registration.passwordCheck("PASSWORD11"));
    }
    @Test
    public void invalidPasswordCheck8(){
        assertFalse(Registration.passwordCheck("password!!"));
    }
    @Test
    public void invalidPasswordCheck9(){
        assertFalse(Registration.passwordCheck("password11"));
    }
    @Test
    public void invalidPasswordCheck10(){
        assertFalse(Registration.passwordCheck("Password!!"));
    }
    @Test
    public void invalidPasswordCheck11(){
        assertFalse(Registration.passwordCheck("Password11"));
    }
}
