import java.util.*;

public class Week1Assignment {


    static HashMap<String, Integer> inventory = new HashMap<>();


    static LinkedHashMap<String, List<Integer>> waitingList = new LinkedHashMap<>();


    public static void checkStock(String productId) {

        int stock = inventory.getOrDefault(productId, 0);

        System.out.println(productId + " → " + stock + " units available");
    }


    public static synchronized void purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {

            inventory.put(productId, stock - 1);

            System.out.println("Success! User " + userId +
                    " purchased " + productId +
                    ". Remaining stock: " + (stock - 1));

        } else {

            waitingList.putIfAbsent(productId, new ArrayList<>());
            waitingList.get(productId).add(userId);

            int position = waitingList.get(productId).size();

            System.out.println("Stock unavailable. User " + userId +
                    " added to waiting list. Position #" + position);
        }
    }


    public static void showWaitingList(String productId) {

        if (waitingList.containsKey(productId)) {

            System.out.println("Waiting List: " + waitingList.get(productId));

        } else {

            System.out.println("No users in waiting list.");
        }
    }

    public static void main(String[] args) {


        inventory.put("IPHONE15_256GB", 100);


        checkStock("IPHONE15_256GB");

        purchaseItem("IPHONE15_256GB", 12345);
        purchaseItem("IPHONE15_256GB", 67890);
        purchaseItem("IPHONE15_256GB", 11111);


        inventory.put("IPHONE15_256GB", 0);

        purchaseItem("IPHONE15_256GB", 99999);


        showWaitingList("IPHONE15_256GB");
    }
}