import java.util.*;

public class Week1Assignment {

    static final int L1_CAPACITY = 10000;
    static final int L2_CAPACITY = 100000;

    static LinkedHashMap<String, String> L1Cache =
            new LinkedHashMap<String, String>(L1_CAPACITY, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > L1_CAPACITY;
                }
            };

    static HashMap<String, String> L2Cache = new HashMap<>();

    static HashMap<String, String> database = new HashMap<>();

    static int L1Hits = 0;
    static int L2Hits = 0;
    static int L3Hits = 0;

    public static String getVideo(String videoId) {

        long start = System.currentTimeMillis();

        if (L1Cache.containsKey(videoId)) {

            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");

            return L1Cache.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        if (L2Cache.containsKey(videoId)) {

            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            String video = L2Cache.get(videoId);

            L1Cache.put(videoId, video);

            System.out.println("Promoted to L1");

            return video;
        }

        System.out.println("L2 Cache MISS");

        if (database.containsKey(videoId)) {

            L3Hits++;

            System.out.println("L3 Database HIT (150ms)");

            String video = database.get(videoId);

            if (L2Cache.size() >= L2_CAPACITY) {

                Iterator<String> it = L2Cache.keySet().iterator();
                if (it.hasNext()) {
                    L2Cache.remove(it.next());
                }
            }

            L2Cache.put(videoId, video);

            return video;
        }

        return null;
    }

    public static void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = (double) L1Hits / total * 100;
        double l2Rate = (double) L2Hits / total * 100;
        double l3Rate = (double) L3Hits / total * 100;

        System.out.println("\nCache Statistics:");

        System.out.println("L1: Hit Rate " + String.format("%.2f", l1Rate) + "%");
        System.out.println("L2: Hit Rate " + String.format("%.2f", l2Rate) + "%");
        System.out.println("L3: Hit Rate " + String.format("%.2f", l3Rate) + "%");

        System.out.println("Overall Requests: " + total);
    }

    public static void main(String[] args) {

        database.put("video_123", "Video Data 123");
        database.put("video_999", "Video Data 999");

        getVideo("video_123");

        getVideo("video_123");

        getVideo("video_999");

        getStatistics();
    }
}