package blockchain.blockchainUtil;

import blockchain.blocks.block;
import blockchain.hasher.hasher256;

import java.util.Iterator;
import java.util.Objects;

public class blockchainValidator {
    public static boolean check(blockchainInstance blockchain) {

        assert blockchain != null;

        Iterator<block> iterator = blockchain.getIterator();

        block currentBlock;

        if(iterator.hasNext())
            currentBlock = iterator.next();
        else return false;

        while(iterator.hasNext()){
            String hashToCheck = hasher256.createHash(currentBlock.getId() + currentBlock.getTimeStamp() + currentBlock.getPreviousHash());
            currentBlock = iterator.next();
            if(!Objects.equals(currentBlock.getPreviousHash(), hashToCheck)) {
                return false;
            }
        }

        return true;
    }
}
