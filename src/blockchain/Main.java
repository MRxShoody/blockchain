package blockchain;

import blockchain.blockchainUtil.blockchainInstance;
import blockchain.commandPattern.Invoker;
import blockchain.commandPattern.command;
import blockchain.commandPattern.commands.blockchainSetup;
import blockchain.commandPattern.commands.generatePOWblock;
import blockchain.messageGenerator.message;
import blockchain.messageGenerator.messageThread;
import blockchain.transaction.transactionThread;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
public class Main {

    public static void main(String[] args) throws InterruptedException {




        messageThread mT = new messageThread();
        transactionThread tT = new transactionThread();
        new Thread(tT).start();

        Invoker invoker = new Invoker();

        command blockchainSetup = new blockchainSetup(true,0);
        command generatepowblock = new generatePOWblock(blockchainInstance.getInstance());

        invoker.setCommand(blockchainSetup);
        invoker.executeCommand();

        invoker.setCommand(generatepowblock);

        invoker.executeCommand();
        Thread.sleep(400);
        for (int i = 0; i < constants.block; i++) {
            invoker.executeCommand();
        }


        tT.stop();

        //Thread.getAllStackTraces().forEach((v,b)->v.interrupt());
    }

}
//        KeyPairGenerator keyGen;
//        try {
//            keyGen = KeyPairGenerator.getInstance("RSA");
//            keyGen.initialize(2048);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        KeyPair keypair = keyGen.generateKeyPair();
//
//        try (FileOutputStream fos = new FileOutputStream("public.key");) {
//            fos.write(Base64.getEncoder().encode(keypair.getPublic().getEncoded()));
//            fos.flush();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        String message = "test";
//
//        try {
//            Signature privateSignature = Signature.getInstance("SHA256withRSA");
//            privateSignature.initSign(keypair.getPrivate());
//            privateSignature.update(message.getBytes(StandardCharsets.UTF_8));
//            byte[] signature = privateSignature.sign();
//
//
//            byte[] keyBytes;
//            try(FileInputStream fis = new FileInputStream("public.key")) {
//                keyBytes = Base64.getDecoder().decode(fis.readAllBytes());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
//
//            Signature publicSignature = Signature.getInstance("SHA256withRSA");
//            publicSignature.initVerify(publicKey);
//            publicSignature.update(message.getBytes(StandardCharsets.UTF_8));
//            boolean isCorrect = publicSignature.verify(signature);
//
//        }catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//
//        //Let's check the signature