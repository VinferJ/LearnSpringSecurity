package me.vinfer.jwtsecurity.provider;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Vinfer
 * @version v1.3
 * @date 2020-09-18    10:05
 * @description
 **/
public class StringEncoder implements PasswordEncoder {

    private final static StringEncoder ENCODER = new StringEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        System.out.println(rawPassword.toString());
        System.out.println(encodedPassword);
        return rawPassword.toString().equals(encodedPassword);
    }

    public static StringEncoder getInstance(){
        return ENCODER;
    }

    private StringEncoder(){}


}
