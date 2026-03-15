import java.util.*;

public class Week1Assignment {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
    }

    static TrieNode root = new TrieNode();

    // query -> frequency
    static HashMap<String, Integer> queryFrequency = new HashMap<>();

    public static void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEnd = true;

        queryFrequency.put(query,
                queryFrequency.getOrDefault(query, 0) + 1);
    }

    public static void collectQueries(TrieNode node, String prefix, List<String> result) {

        if (node.isEnd) {
            result.add(prefix);
        }

        for (char c : node.children.keySet()) {
            collectQueries(node.children.get(c), prefix + c, result);
        }
    }

    public static List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        List<String> queries = new ArrayList<>();

        collectQueries(node, prefix, queries);

        PriorityQueue<String> pq =
                new PriorityQueue<>((a, b) ->
                        queryFrequency.get(b) - queryFrequency.get(a));

        pq.addAll(queries);

        List<String> topResults = new ArrayList<>();

        int count = 0;

        while (!pq.isEmpty() && count < 10) {
            topResults.add(pq.poll());
            count++;
        }

        return topResults;
    }

    public static void updateFrequency(String query) {

        queryFrequency.put(query,
                queryFrequency.getOrDefault(query, 0) + 1);

        System.out.println(query + " → Frequency: " +
                queryFrequency.get(query));
    }

    public static void main(String[] args) {

        insertQuery("java tutorial");
        insertQuery("javascript");
        insertQuery("java download");
        insertQuery("java tutorial");
        insertQuery("java tutorial");
        insertQuery("java 21 features");

        System.out.println("Suggestions for 'jav':");

        List<String> suggestions = search("jav");

        int rank = 1;

        for (String s : suggestions) {
            System.out.println(rank + ". " + s +
                    " (" + queryFrequency.get(s) + " searches)");
            rank++;
        }

        updateFrequency("java 21 features");
        updateFrequency("java 21 features");
    }
}