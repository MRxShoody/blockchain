package blockchain.miner;

import blockchain.blockchainUtil.blockchainInstance;
import blockchain.hasher.hasher256;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class miner implements Runnable {


    private static ThreadLocal<String> messages;
    private static final blockchainInstance blockchain = blockchainInstance.getInstance();
    private static volatile boolean isRunning;
    private static volatile int currentId;
    private static volatile String newTimeStamp;
    private static volatile String previousHash = "0";
    private static final AtomicInteger magicNumber = new AtomicInteger(0);

    private final CyclicBarrier cb;
    private final CountDownLatch cdl;
    private final int id;
    public miner(CyclicBarrier cb, CountDownLatch cdl, int id, String vmessages){
        this.cb = cb;
        this.cdl = cdl;
        this.id = id;
        messages = ThreadLocal.withInitial(() -> vmessages);
    }

    public static void minerSetup(){
        magicNumber.set(0);
        isRunning = true;
        miner.currentId = blockchain.blockchain.size() + 1;
        miner.newTimeStamp = Long.toString(System.currentTimeMillis());
    }

    @Override
    public void run() {
        try {
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Instant t1 = Instant.now();

        while (isRunning){
            final int finalMagicNumber = magicNumber.incrementAndGet();

            String newHash = hasher256.createHash(currentId + newTimeStamp + previousHash + finalMagicNumber + messages.get());

            if (newHash.startsWith(blockchain.difficulty) && isRunning) {
                isRunning = false;

                Instant t2 = Instant.now();

                Duration d = Duration.between(t1, t2);

                blockchain.addMinerBlock(currentId, newTimeStamp, previousHash, finalMagicNumber,d.getSeconds(), id, messages.get());
                previousHash = newHash;
            }
        }

        cdl.countDown();


    }
}