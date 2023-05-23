package blockchain.messageGenerator;

import java.security.PublicKey;

public record message(String id, String name, String message, byte[] signature, PublicKey publicKey) {

    public message(String name, String message) {
        this(null, name, message, null, null);
    }
    public String getMessage() {
        return String.format("%s %s:\n%s",id, name, message);
    }
}
