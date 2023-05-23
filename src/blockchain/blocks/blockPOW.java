package blockchain.blocks;

import blockchain.constants;

public class blockPOW extends blockNoPOW{


    private final int magicNumber;

    private final int miner;
    private final long generationTime;
    private String messages;

    public blockPOW(int id, String timestamp, String previousHash, String hash, int magicNumber, long generationTime, int miner) {
        super(id, timestamp, previousHash, hash);
        this.magicNumber = magicNumber;
        this.generationTime = generationTime;
        this.miner = miner;
    }

    public blockPOW(int id, String timestamp, String previousHash, String hash, int magicNumber, long generationTime, int miner, String messages) {
        super(id, timestamp, previousHash, hash);
        this.magicNumber = magicNumber;
        this.generationTime = generationTime;
        this.miner = miner;
        if(messages.equals("")) {
            this.messages = "no transactions";
        }else {
            this.messages = "\n"+messages;
        }
    }

    public String toString() {
        return String.format(
            """
            Block:
            Created by miner%d
            miner%d gets %d VC
            Id: %d
            Timestamp: %s
            Magic number: %d
            Hash of the previous block:
            %s
            Hash of the block:
            %s   
            Block data: %s            
            Block was generating for %d seconds   
            """,
            miner,miner,constants.BlockReward,id,timestamp,magicNumber,previousHash,hash,messages,generationTime);
    }
}
