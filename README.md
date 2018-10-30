# CS1699 Deliverable 2: Mining

### Author
- Adam Richman

### GitHub Username
- adamrichman1

### Project URL
- https://github.com/adamrichman1/CS1699_Project2

### How I maximized transaction fees:
My mining program maximizes mining fees by looking at the average reward per transaction input/output. 
The way I do this is by sorting all transactions based on v where v = (transaction fees) / (total inputs + total outputs).
I implemented the compareTo method of the Comparable<T> interface within the Transaction class to accomplish this.
I originally included just the transactions with the most transaction fees but was concerned this would have reduced
potential rewards since extra space could be leftover in the block.