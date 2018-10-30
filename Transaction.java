public class Transaction implements Comparable<Transaction> {

    private int reward;
    private String transaction;
    private int size;

    Transaction(String transaction, String[] inputs, String[] outputs) {
        int input = 0, output = 0;
        for (String in: inputs) {
            input += Integer.parseInt(in.split(">")[1]);
        }
        for (String out: outputs) {
            output += Integer.parseInt(out.split(">")[1]);
        }
        reward = input - output;
        size = inputs.length + outputs.length;
        this.transaction = transaction;
    }

    int getReward() {
        return reward;
    }

    int getSize() {
        return size;
    }

    @Override
    public int compareTo(Transaction transaction) {
        if (reward/size < transaction.getReward()/transaction.getSize()) {
            return 1;
        }
        else if (reward/size > transaction.getReward()/transaction.getSize()) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return transaction;
    }
}
