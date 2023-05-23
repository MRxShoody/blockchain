package blockchain.blocks;

public interface block {
    public int getId();

    public String getTimeStamp();

    public String getHash();

    public String getPreviousHash();

    public String toString();

}
