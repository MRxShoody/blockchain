package blockchain.blocks;

public class blockNoPOW implements block{

    final int id;

    final String timestamp;

    final String hash;

    final String previousHash;


    public blockNoPOW(int id, String timestamp, String previousHash, String hash) {
        this.id = id;
        this.timestamp = timestamp;
        this.hash = hash;
        this.previousHash = previousHash;
    }

    public int getId() {
        return id;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    @Override
    public String toString() {
        return String.format(
                """
                Block:
                Id: %d
                Timestamp: %s
                Hash of the previous block:
                %s
                Hash of the block:
                %s                         
                """,
                id,timestamp,previousHash,hash);
    }
}
