package blockchain.commandPattern.commands;

import blockchain.blockchainUtil.blockchainInstance;
import blockchain.commandPattern.command;
import blockchain.constants;
import blockchain.miner.miner;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class generatePOWblock implements command {

    blockchainInstance blockchain;
    public generatePOWblock(blockchainInstance blockchain){
        this.blockchain = blockchain;
    }
    @Override
    public void execute() {
        blockchain.alreadyAdded = false;


        miner.minerSetup();
        int numberOfThreads = constants.minerCount;

        CyclicBarrier cb = new CyclicBarrier(numberOfThreads + 1);
        CountDownLatch cdl = new CountDownLatch(numberOfThreads);

        String message = blockchain.getMessages().stream().reduce((a,b) -> a + "\n" + b).orElse("");

        //System.out.println(message);

        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(new miner(cb, cdl, i, message)).start();
        }

        try {
            cb.await();
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
