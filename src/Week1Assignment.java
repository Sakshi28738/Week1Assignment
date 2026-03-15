import java.util.*;

public class Week1Assignment {

    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    static HashMap<String, DNSEntry> cache = new HashMap<>();

    static int cacheHits = 0;
    static int cacheMisses = 0;

    // Simulated upstream DNS
    public static String queryUpstreamDNS(String domain) {

        String ip = "172.217.14." + new Random().nextInt(255);

        System.out.println("Cache MISS → Query upstream → " + ip);

        return ip;
    }

    public static String resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {

                cacheHits++;
                System.out.println("Cache HIT → " + entry.ipAddress);

                return entry.ipAddress;

            } else {

                System.out.println("Cache EXPIRED for " + domain);
                cache.remove(domain);
            }
        }

        cacheMisses++;

        String newIP = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, newIP, 5); // TTL = 5 seconds
        cache.put(domain, newEntry);

        return newIP;
    }

    public static void cleanExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, DNSEntry> entry = iterator.next();

            if (entry.getValue().isExpired()) {

                iterator.remove();
                System.out.println("Removed expired entry: " + entry.getKey());
            }
        }
    }

    public static void getCacheStats() {

        int total = cacheHits + cacheMisses;

        double hitRate = (total == 0) ? 0 : ((double) cacheHits / total) * 100;

        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        resolve("google.com");

        resolve("google.com");

        Thread.sleep(6000);

        resolve("google.com");

        cleanExpiredEntries();

        getCacheStats();
    }
}