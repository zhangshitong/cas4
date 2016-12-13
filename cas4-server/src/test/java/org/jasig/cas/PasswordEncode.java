package org.jasig.cas;

import org.jasig.cas.authentication.handler.SaltPasswordEncoder;

/**
 * Created by stzhang on 2016/12/13.
 */
public class PasswordEncode {
    public static void main(String args[]){
        SaltPasswordEncoder encoder = new SaltPasswordEncoder("MD5");
        encoder.setCharacterEncoding("utf-8");
        encoder.setEncodeSalt("chinausa");
        System.out.print(encoder.encode("123456"));


    }
}
