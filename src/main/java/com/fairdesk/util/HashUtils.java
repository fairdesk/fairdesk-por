package com.fairdesk.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public static String getUserHash(String uid, String nonce, String balance) {
        String merkelHash = uid + "," + balance + "," + nonce;
        return sha256(merkelHash).substring(0, 16);
    }

    public static String sha256(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            return byteToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private static String byteToHex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        for (byte element : b) {
            String hexString = (Integer.toHexString(element & 0XFF));
            if (hexString.length() == 1) {
                hs.append("0").append(hexString);
            } else {
                hs.append(hexString);
            }
        }
        return hs.toString();
    }
}
