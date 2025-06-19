package com.resale.app.utility;


import com.tasktk.app.utility.EncryptText;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Priority(1)
@Alternative
public class EncryptSha256 implements EncryptText {

    public String encrypt(String text) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        messageDigest.update(text.getBytes());

        return new String(messageDigest.digest());
    }
}