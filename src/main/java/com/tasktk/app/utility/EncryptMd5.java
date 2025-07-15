package com.tasktk.app.utility;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Priority(100)
@Alternative
public class EncryptMd5 implements EncryptText {

    @Override
    public String encrypt(String text) {

        String encryptedText = null;

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");

            m.update(text.getBytes());
            byte[] bytes = m.digest();

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            encryptedText = s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return encryptedText;
    }
}