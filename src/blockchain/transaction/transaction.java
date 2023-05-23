package blockchain.transaction;

import java.security.PublicKey;

public record transaction(int id,int from, int to, int amount, byte[] signature, PublicKey publicKey) {
}
