package ch.abbts.sya;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Miner {
    private String difficulty = "0";

    public Long getNonce() {
        return theNonce;
    }

    private Long theNonce = 0L;


    public void setDifficulty(int diff) {
        difficulty = "0";
        if (diff < 1) diff = 1;
        for (int i = 1; i < diff; i++) {
            difficulty += "0";
        }
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String hashString(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodehash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodehash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String mineHash(String messageHash) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodehash = null;
        String hashAndNonce;

        for (int nonce = 0; nonce <= Integer.MAX_VALUE; nonce++) {
            hashAndNonce = messageHash + Integer.toString(nonce);

            encodehash = digest.digest(
                    hashAndNonce.getBytes(StandardCharsets.UTF_8));
            String hash = bytesToHex(encodehash);
            if (hash.startsWith(getDifficulty())) {
                System.out.println("Found Nonce: " + nonce);
                theNonce = (long)nonce;
                break;
            }

            if (nonce == Integer.MAX_VALUE) {
                System.out.println("Could not find a nonce!");
                theNonce = -1L;
                break;
            }
        }
        return bytesToHex(encodehash);
    }

}
