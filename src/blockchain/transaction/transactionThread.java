package blockchain.transaction;

import blockchain.blockchainUtil.blockchainInstance;
import blockchain.constants;
import blockchain.messageGenerator.message;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class transactionThread implements Runnable{
    private final List<Integer> miners;

    private volatile boolean isRunning = true;

    public transactionThread() {
        miners = new ArrayList<>();
        IntStream.iterate(0, i -> i + 1).limit(constants.minerCount).forEach(miners::add);
    }
    public void stop(){
        isRunning = false;
    }

    @Override
    public void run() {

        int id = 0;

        Signature signature;
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            signature = Signature.getInstance("SHA256withRSA");
            keyPair = keyPairGenerator.generateKeyPair();
            signature.initSign(keyPair.getPrivate());

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        while (isRunning) {
            id++;
            try {
                Thread.sleep((long) (Math.random()*10));

                Integer from = miners.get((int) (Math.random() * miners.size()));

                Integer to;
                do {
                    to = miners.get((int) (Math.random() * miners.size()));
                } while (to.equals(from));

                int amount = (int) (Math.random() * 100);

                String messageToSign = Integer.toString(id) + from + to + amount;
                signature.update(messageToSign.getBytes(StandardCharsets.UTF_8));

                blockchainInstance.getInstance().addTransaction(
                        new transaction(
                                id,
                                from,
                                to,
                                amount,
                                signature.sign(),
                                keyPair.getPublic()
                        ));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
