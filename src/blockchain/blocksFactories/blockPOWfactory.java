package blockchain.blocksFactories;

import blockchain.blocks.block;
import blockchain.blocks.blockPOW;
import blockchain.hasher.hasher256;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class blockPOWfactory extends blockFactory {

    private final String zeros;

    public blockPOWfactory(String difficulty) {
        zeros = difficulty;
    }

    @Override
    public block createBlock() {
        String newTimeStamp = createTimeStamp();

        String newHash;
        int calculatedMagicNumber = 0;

        currentId++;

        Instant t1 = Instant.now();

        while(true){
            newHash = hasher256.createHash(currentId + newTimeStamp + previousHash + calculatedMagicNumber);
            if(newHash.startsWith(zeros)){
                break;
            }
            calculatedMagicNumber++;
        }

        Instant t2 = Instant.now();
        Duration d = Duration.between(t1, t2);

        return new blockPOW(currentId, newTimeStamp, previousHash, previousHash = newHash,calculatedMagicNumber,d.getSeconds(),1);
    }

}
