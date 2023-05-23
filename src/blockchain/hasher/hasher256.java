package blockchain.hasher;

import java.security.MessageDigest;

public class hasher256 {
    public static String createHash(String input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            StringBuilder hexString = new StringBuilder();
            for (byte elem: digest.digest(input.getBytes("UTF-8"))) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
