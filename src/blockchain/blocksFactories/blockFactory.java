package blockchain.blocksFactories;

import blockchain.blocks.block;

public abstract class blockFactory {

    static int currentId;
    static String previousHash;

    static
    {
        currentId = 0;
        previousHash = "0";
    }
    abstract block createBlock();

    public block createAndGetBlock(){
        return createBlock();
    }

    static String createTimeStamp(){
        return Long.toString(System.currentTimeMillis());
    }

    public static int getCurrentId(){
        return ++currentId;
    }
}

