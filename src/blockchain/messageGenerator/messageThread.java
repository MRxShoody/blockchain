package blockchain.messageGenerator;

import blockchain.blockchainUtil.blockchainInstance;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.List;
import java.util.stream.Collectors;

public class messageThread implements Runnable {

    private final List<String> names = List.of("Sophie", "Diana","George", "Jean");
    private final List<String> words = List.of("name", "age", "city", "university","hi",
            "lol","ikr","plane","car","house",
            "money","cash","traffic","dinner","food","oof","kekw");

    private volatile boolean isRunning = true;
    public messageThread() {
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

                String ide = Integer.toString(id);
                String name = names.get((int) (Math.random() * names.size()));
                String message = words.stream()
                        .skip(Math.round((float) Math.random() * words.size()))
                        .limit(Math.round((float) Math.random() * words.size()))
                        .collect(Collectors.joining(" "));

                String messageToSign = ide+name+message;
                signature.update(messageToSign.getBytes(StandardCharsets.UTF_8));

                blockchainInstance.getInstance().addMessage(
                    new message(
                        ide,
                        name,
                        message,
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
