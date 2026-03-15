import java.util.*;

public class Week1Assignment {


    static HashMap<String, Integer> pageViews = new HashMap<>();


    static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();


    static HashMap<String, Integer> trafficSources = new HashMap<>();


    public static void processEvent(String url, String userId, String source) {


        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);


        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);


        trafficSources.put(source,
                trafficSources.getOrDefault(source, 0) + 1);
    }


    public static List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        List<Map.Entry<String, Integer>> topPages = new ArrayList<>();

        int count = 0;

        while (!pq.isEmpty() && count < 10) {
            topPages.add(pq.poll());
            count++;
        }

        return topPages;
    }


    public static void getDashboard() {

        System.out.println("\nTop Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> page : topPages) {

            String url = page.getKey();
            int views = page.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;

        for (int count : trafficSources.values()) {
            total += count;
        }

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);

            double percentage = ((double) count / total) * 100;

            System.out.println(source + ": " +
                    String.format("%.2f", percentage) + "%");
        }
    }

    public static void main(String[] args) {

        processEvent("/article/breaking-news", "user_123", "Google");
        processEvent("/article/breaking-news", "user_456", "Facebook");
        processEvent("/sports/championship", "user_111", "Direct");
        processEvent("/sports/championship", "user_222", "Google");
        processEvent("/sports/championship", "user_111", "Google");
        processEvent("/tech/ai-update", "user_333", "Google");

        getDashboard();
    }
}