import java.math.BigInteger;
import java.util.Date;

class Block {

    private String prevHash;
    private String timestamp;
    private String numTransactions;
    private BigInteger difficulty;
    private String concatRoot;
    private BigInteger block;
    private String transactionList;
    private int size;
    private int reward = 50;

    Block(Transaction[] transactions, String prevHash, BigInteger difficulty) {
        this.prevHash = prevHash;
        timestamp = Long.toString(new Date().getTime());
        numTransactions = Integer.toString(transactions.length);
        this.difficulty = difficulty;
        concatRoot = buildConcatRoot(transactions);
        buildBlock("0000");
    }

    private String buildConcatRoot(Transaction[] transactions) {
        StringBuilder transList = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (Transaction transaction: transactions) {
            if (size + transaction.getSize() >= 16) {
//                System.out.println("$$$ Current block size: " + size);
//                System.out.println("$$$ Excluding transaction: " + transaction.getSize());
                continue;
            }
//            System.out.println("$$$ Current block size: " + size);
//            System.out.println("$$$ Adding transaction: " + transaction.getSize());
            sb.append(transaction.toString());
            transList.append(transaction.toString()).append("\n");
            size += transaction.getSize();
            reward += transaction.getReward();
        }
        // Add coinbase transaction
        sb.append(";1333dGpHU6gQShR596zbKHXEeSihdtoyLb>").append(reward);
        transList.append(";1333dGpHU6gQShR596zbKHXEeSihdtoyLb>").append(reward);
        size++;

        transactionList = transList.toString();
//        System.out.println("Block size: " + size);
//        System.out.println("Block reward: " + reward);
        return Sha256Hash.calculateHash(sb.toString());
    }

    void buildBlock(String nonce) {
        block = Sha256Hash.hashBigInteger(prevHash + numTransactions + timestamp + difficulty + nonce + concatRoot);
    }

    BigInteger getHash() {
        return block;
    }

    public BigInteger getDifficulty() {
        return difficulty;
    }

    public String getConcatRoot() {
        return concatRoot;
    }

    public int getSize() {
        return size;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTransactionList() {
        return transactionList;
    }
}
