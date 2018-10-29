import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Mine {

    private static String blockchain;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println(">>> ERROR: Run program with java Mine *candidate_transaction_file* *difficulty* *prev_hash*");
            System.exit(1);
        }
        Transaction[] transactions = readTransactions(args[0]);
        assert transactions != null;
        System.out.println("$$$ Transactions read in successfully");

        Arrays.sort(transactions);
        System.out.println("$$$ Sorted transactions based on miner reward");
        System.out.println(Arrays.toString(transactions));

        mine(transactions, args[1]);
        System.out.println("$$$ Successfully mined block");
    }

    private static Transaction[] readTransactions(String filename) {
        try {
            ArrayList<Transaction> transactions = new ArrayList<>();
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                String transaction = scanner.nextLine();
                String[] transactionParts = transaction.split(";");
                String[] inputs = transactionParts[0].split(",");
                String[] outputs = transactionParts[1].split(",");
                transactions.add(new Transaction(transaction, inputs, outputs));
            }
            return transactions.toArray(new Transaction[transactions.size()]);
        } catch (FileNotFoundException e) {
            System.err.println(">>> ERROR: Invalid configuration file provided: " + filename);
            System.exit(1);
            return null;
        }
    }

    private static void addBlock(String block) {

    }

    private static void mine(Transaction[] transactions, String difficulty) {
        String nonce = "1234";
        StringBuilder blockBuilder = new StringBuilder();
        for (Transaction transaction: transactions) {
            blockBuilder.append(transaction.toString());
        }
        String block = Sha256Hash.calculateHash(blockBuilder.toString());
        BigInteger target = new BigInteger(difficulty, 16);
        boolean lookingForTarget = true;
        String concat;
        BigInteger hash;

        BigInteger numHashes = new BigInteger("0");
        long startTime = System.nanoTime();

        // Keep looping until we find target...
        while (lookingForTarget) {
            // Increment number of hashes tried
            numHashes = numHashes.add(BigInteger.ONE);
            // Concatenate our nonce and our block
            concat = (leftPad(nonce, 64)) + block;

            // Uncomment to see failed attempts
            // System.out.println("Trying: " + concat);

            // Calculate the hash.  Kept in a BigInteger since we will want to compare it.
            hash = Sha256Hash.hashBigInteger(concat);

            // If our hash is less than the target, we have succeeded
            if (hash.compareTo(target) == -1) {
                long endTime = System.nanoTime();
                printHashRate(numHashes, startTime, endTime);
                lookingForTarget = false;
                System.out.println("Nonce       = " + nonce);
                System.out.println("Full block  = " + concat);
                System.out.println("Hash(block) = " + leftPad(hash.toString(16), 64));
            } else {
                // Uncomment to see failed attempts
                // System.out.println("Fail, hash "
                //            + leftPad(hash.toString(16), 64) + " >= "
                //            + leftPad(target.toString(16), 64));
                nonce = incrementStringNonce(nonce);
            }
        }
    }

    /**
     * This increments our String nonce by accepting a String version
     * and returning a String version.  For example:
     * "000A" -> "000B"
     * "FFFE" -> "FFFF"
     * @param nonce initial nonce
     * @return nonce incremented by one in string form
     */
    private static String incrementStringNonce(String nonce) {
        BigInteger bi = new BigInteger(nonce, 16);
        bi = bi.add(BigInteger.ONE);
        return bi.toString(16);
    }

    /**
     * Prepend a string with 0's until it is of length n.
     * Useful for printing out hash results.
     * @param str String to prepend 0's to
     * @param n correct size of string after being padded
     * @return String str left-padded with 0's
     */
    private static String leftPad(String str, int n) {
        return String.format("%1$" + n + "s", str).replace(' ', '0');
    }

    /**
     * Given a start time and end time in nanoseconds (courtesy of System.nanoTime),
     * and a number of hashes complete in this time, print out the number of hashes
     * per second.
     * @param numHashes - number of hashes completed
     * @param startTime - time hashing started
     * @param endTime - time hashing ended
     */
    private static void printHashRate(BigInteger numHashes,
                                     long startTime,
                                     long endTime) {
        long timeDiff = endTime - startTime;
        long seconds = timeDiff / 1000000000;
        BigInteger time = new BigInteger ((Long.valueOf(seconds)).toString());
        BigInteger hashesPerSecond = numHashes.divide(time);
        System.out.println("Hash rate = " + hashesPerSecond + " hashes per second");

    }
}
