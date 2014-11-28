package be.ordina.sest.homearchive.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.log4j.Log4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

/**
 * Utilities for operations with security token
 * Created by sest on 25/11/14.
 *
 */
@Log4j
public class TokenUtils {
    public static final String MAGIC_KEY = "verysecretword";

    /**
     * Creates security token
     * @param userDetails {@link org.springframework.security.core.userdetails.UserDetails}
     * @return token
     */
    public static String createToken(UserDetails userDetails) {
        /* Expires in one hour */
        long expires = System.currentTimeMillis() + 1000L * 60 * 60;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires));

        return tokenBuilder.toString();
    }

    /**
     * Computes signature
     * @param userDetails {@link org.springframework.security.core.userdetails.UserDetails}
     * @param expires expiry period
     * @return user signature
     */
    public static String computeSignature(UserDetails userDetails, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(userDetails.getPassword());
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtils.MAGIC_KEY);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    /**
     * Gets username from token
     * @param authToken token
     * @return username
     */
    public static String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }

        String[] parts = authToken.split(":");
        return parts[0];
    }

    /**
     * Validates token
     * @param authToken token
     * @param userDetails {@link org.springframework.security.core.userdetails.UserDetails}
     * @return returns true if signature is valid
     */
    public static boolean validateToken(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }
        log.debug("Signature extracted from the token: " + signature);
        log.debug("Computed signature: " + TokenUtils.computeSignature(userDetails, expires));
        return signature.equals(TokenUtils.computeSignature(userDetails, expires));
    }
}
