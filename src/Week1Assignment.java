import java.util.*;

public class Week1Assignment {


    static HashMap<String, Integer> usernameMap = new HashMap<>();


    static HashMap<String, Integer> attemptFrequency = new HashMap<>();


    public static boolean checkAvailability(String username) {


        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);


        return !usernameMap.containsKey(username);
    }


    public static List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username + "123");
        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    public static String getMostAttempted() {

        String mostAttempted = "";
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + maxAttempts + " attempts)";
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        usernameMap.put("john_doe", 101);
        usernameMap.put("admin", 102);
        usernameMap.put("alex123", 103);

        System.out.print("Enter username to check: ");
        String username = sc.nextLine();

        if (checkAvailability(username)) {

            System.out.println("Username available!");

        } else {

            System.out.println("Username already taken.");
            System.out.println("Suggested usernames: "
                    + suggestAlternatives(username));
        }

        System.out.println("Most attempted username: "
                + getMostAttempted());

        sc.close();
    }
}