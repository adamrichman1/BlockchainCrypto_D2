public class Transaction implements Comparable<Transaction> {

    private int reward;
    private String transaction;
    private int input = 0;
    private int output = 0;

    Transaction(String transaction, String[] inputs, String[] outputs) {
        for (String in: inputs) {
            input += Integer.parseInt(in.split(">")[1]);
        }
        for (String out: outputs) {
            output += Integer.parseInt(out.split(">")[1]);
        }
        reward = input - output;
        this.transaction = transaction;
    }

    private int getReward() {
        return reward;
    }

    @Override
    public int compareTo(Transaction transaction) {
        if (reward < transaction.getReward()) {
            return 1;
        }
        else if (reward > transaction.getReward()) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return transaction;
    }
}
