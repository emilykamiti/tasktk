package com.tasktk.app.utility;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Priority(1)
@Alternative
public class EncryptSha256 implements EncryptText {
    public String encrypt(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            return bytesToHex(messageDigest.digest()); // Fixed: Use hex instead of new String()
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 encryption failed", e);
        }
    }

    // Helper to convert bytes to hex (safe for storage)
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}