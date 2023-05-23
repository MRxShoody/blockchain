package blockchain.blocksFactories;

import blockchain.blocks.block;
import blockchain.blocks.blockNoPOW;
import blockchain.hasher.hasher256;

public class blockNoPOWfactory extends blockFactory {


    @Override
    public block createBlock(){
        String newTimeStamp = createTimeStamp();

        String newHash = hasher256.createHash(++currentId + newTimeStamp + previousHash);

        return new blockNoPOW(currentId, newTimeStamp, previousHash, previousHash = newHash);
    }

}
