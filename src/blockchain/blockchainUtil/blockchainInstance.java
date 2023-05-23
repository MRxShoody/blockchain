package blockchain.blockchainUtil;

import blockchain.blocks.block;
import blockchain.blocks.blockPOW;
import blockchain.blocksFactories.blockFactory;
import blockchain.blocksFactories.blockNoPOWfactory;
import blockchain.blocksFactories.blockPOWfactory;
import blockchain.constants;
import blockchain.hasher.hasher256;
import blockchain.messageGenerator.message;
import blockchain.transaction.transaction;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.*;

public class blockchainInstance {

    private boolean isPOW = false;
    private blockFactory blocksFactory = new blockNoPOWfactory();
    public String difficulty;
    public boolean alreadyAdded = false;

    public final HashSet<block> blockchain = new LinkedHashSet<>();

    private final List<String> messages = new ArrayList<>();

    private final HashMap<Integer,Integer> minersWallet = new HashMap<>();

    private static final blockchainInstance instance = new blockchainInstance();
    private blockchainInstance() {
        for (int i = 0 ; i < constants.minerCount; i++) {
            minersWallet.put(i,constants.initialWallet);
        }
    }
    public static blockchainInstance getInstance() {return instance;}

    public void setDifficulty(int difficulty) {
        if(!isPOW){
            throw new RuntimeException("You can't set difficulty for blockchain without POW");
        }
        this.difficulty = "0".repeat(difficulty);
        blocksFactory = new blockPOWfactory(this.difficulty);
    }


    public void isPOW(boolean bool){
        isPOW = bool;

        if(!isPOW){
            blocksFactory = new blockNoPOWfactory();
        }
    }

    public void addNoMinerNblocks(int n){
        for (int i = 0; i < n; i++) {
            blockchain.add(blocksFactory.createAndGetBlock());
        }
    }

    public synchronized void addMinerBlock(int currentId, String newTimeStamp, String previousHash, int finalMagicNumber, long generationTime, int miner, String messages){
        if (alreadyAdded) {
            return;
        }

        alreadyAdded = true;

        String newHash;

        synchronized (this){
            newHash = hasher256.createHash(currentId + newTimeStamp + previousHash + finalMagicNumber+ messages);
        }

        if (!newHash.startsWith(difficulty)) {
            throw new RuntimeException("Block doesn't match difficulty");
        }
        String message = "N stayed the same";
        if(generationTime > 60){
            difficulty = "0".repeat(difficulty.length() - 1);
            message = "N was decreased by 1";
        }else if(generationTime < 10){
            difficulty = "0".repeat(difficulty.length() + 1);
            message = "N was increased to " + difficulty.length();
        }

        block lastblock = new blockPOW
        (
                currentId,
                newTimeStamp,
                previousHash,
                newHash ,
                finalMagicNumber,
                generationTime,
                miner,
                messages
        );

        minersWallet.put(miner,minersWallet.get(miner) + constants.BlockReward);
        blockchain.add(lastblock);
        minersWallet.put(miner,minersWallet.get(miner) + constants.BlockReward);
        System.out.print(lastblock);
        System.out.println(message+"\n");
    }

    public void printBlockchain(){
        for (block block : blockchain) {
            System.out.println(block.toString());
        }
    }

    public Iterator<block> getIterator() {
        return blockchain.iterator();
    }

    public void addMessage(message message) {
        try {

            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(message.publicKey());
            String messageToVerify = message.id()+ message.name() + message.message();
            publicSignature.update(messageToVerify.getBytes(StandardCharsets.UTF_8));

            boolean isCorrect = publicSignature.verify(message.signature());
            if(isCorrect)
                messages.add(message.name() + " : " + message.message());
            else
                throw new RuntimeException("Signature incorrect");
            //System.out.println("Signature correct: " + isCorrect);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //System.out.println(message.name() + " : " + message.message());;
    }

    public void addTransaction(transaction transaction){
        try {
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(transaction.publicKey());
            String messageToVerify = Integer.toString(transaction.id()) + transaction.from() + transaction.to() + transaction.amount();

            publicSignature.update(messageToVerify.getBytes(StandardCharsets.UTF_8));

            boolean isCorrect = publicSignature.verify(transaction.signature());
            if(isCorrect) {
                if (messages.size() < constants.maxTransactionNumber && minersWallet.get(transaction.from()) > transaction.amount()) {
                    minersWallet.put(transaction.from(), minersWallet.get(transaction.from()) - transaction.amount());
                    minersWallet.put(transaction.to(), minersWallet.get(transaction.to()) + transaction.amount());
                    messages.add("miner" + transaction.from() + " sent " + transaction.amount() + "VC to miner " + transaction.to());
                }
            }
            else
                throw new RuntimeException("Signature incorrect");
            //System.out.println("Signature correct: " + isCorrect);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getMessages(){
        List<String> temp = new ArrayList<>(messages);
        messages.clear();
        return temp;
    }
}