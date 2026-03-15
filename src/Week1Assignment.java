import java.util.*;

public class Week1Assignment {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        int time; // minutes from start of day

        Transaction(int id, int amount, String merchant, String account, int time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    // Classic Two-Sum
    public static void findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction match = map.get(complement);

                System.out.println("Two-Sum Match → (" +
                        match.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum with time window (1 hour = 60 minutes)
    public static void findTwoSumTimeWindow(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= 60) {

                    System.out.println("Time Window Match → (" +
                            prev.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate transaction detection
    public static void detectDuplicates(List<Transaction> transactions) {

        HashMap<String, List<String>> duplicates = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            duplicates.putIfAbsent(key, new ArrayList<>());

            duplicates.get(key).add(t.account);
        }

        for (String key : duplicates.keySet()) {

            List<String> accounts = duplicates.get(key);

            if (accounts.size() > 1) {

                System.out.println("Duplicate Transaction → " +
                        key + " accounts: " + accounts);
            }
        }
    }

    // K-Sum (simple recursive approach)
    public static void findKSum(List<Transaction> transactions, int k, int target,
                                List<Transaction> current, int index) {

        if (k == 0 && target == 0) {

            System.out.print("K-Sum Match → ");

            for (Transaction t : current) {
                System.out.print(t.id + " ");
            }

            System.out.println();
            return;
        }

        if (k == 0 || index >= transactions.size()) {
            return;
        }

        Transaction t = transactions.get(index);

        current.add(t);

        findKSum(transactions, k - 1, target - t.amount, current, index + 1);

        current.remove(current.size() - 1);

        findKSum(transactions, k, target, current, index + 1);
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "Store A", "acc1", 600));
        transactions.add(new Transaction(2, 300, "Store B", "acc2", 615));
        transactions.add(new Transaction(3, 200, "Store C", "acc3", 630));
        transactions.add(new Transaction(4, 500, "Store A", "acc4", 640));

        System.out.println("Two-Sum Results:");
        findTwoSum(transactions, 500);

        System.out.println("\nTwo-Sum with Time Window:");
        findTwoSumTimeWindow(transactions, 500);

        System.out.println("\nDuplicate Detection:");
        detectDuplicates(transactions);

        System.out.println("\nK-Sum Results:");
        findKSum(transactions, 3, 1000, new ArrayList<>(), 0);
    }
}