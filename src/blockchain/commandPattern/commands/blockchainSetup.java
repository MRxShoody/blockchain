package blockchain.commandPattern.commands;

import blockchain.blockchainUtil.blockchainInstance;
import blockchain.commandPattern.command;

public class blockchainSetup implements command {

    private boolean isPow = false;
    private int difficulty = 0;
    public blockchainSetup(boolean isPow, int difficulty){
        this.isPow = isPow;
        this.difficulty = difficulty;
    }

    @Override
    public void execute() {
        blockchainInstance blockchain = blockchainInstance.getInstance();
        blockchain.isPOW(isPow);
        blockchain.setDifficulty(difficulty);
    }
}
