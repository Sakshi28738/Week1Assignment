import java.util.*;

public class Week1Assignment {

    static class TokenBucket {
        int tokens;
        int maxTokens;
        double refillRatePerSecond;
        long lastRefillTime;

        TokenBucket(int maxTokens, double refillRatePerSecond) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRatePerSecond = refillRatePerSecond;
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean allowRequest() {
            refillTokens();

            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        synchronized void refillTokens() {
            long now = System.currentTimeMillis();
            double secondsPassed = (now - lastRefillTime) / 1000.0;

            int tokensToAdd = (int) (secondsPassed * refillRatePerSecond);

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }

        synchronized int getRemainingTokens() {
            refillTokens();
            return tokens;
        }

        synchronized int getUsedTokens() {
            refillTokens();
            return maxTokens - tokens;
        }

        synchronized long getRetryAfterSeconds() {
            refillTokens();

            if (tokens > 0) {
                return 0;
            }

            return (long) Math.ceil(1.0 / refillRatePerSecond);
        }

        synchronized long getResetTime() {
            refillTokens();
            int missingTokens = maxTokens - tokens;
            return System.currentTimeMillis() / 1000
                    + (long) Math.ceil(missingTokens / refillRatePerSecond);
        }
    }

    static HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    static final int LIMIT_PER_HOUR = 1000;
    static final double REFILL_RATE_PER_SECOND = 1000.0 / 3600.0;

    public static synchronized String checkRateLimit(String clientId) {
        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(LIMIT_PER_HOUR, REFILL_RATE_PER_SECOND));

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining, retry after "
                    + bucket.getRetryAfterSeconds() + "s)";
        }
    }

    public static synchronized void getRateLimitStatus(String clientId) {
        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(LIMIT_PER_HOUR, REFILL_RATE_PER_SECOND));

        TokenBucket bucket = clientBuckets.get(clientId);

        System.out.println("{used: " + bucket.getUsedTokens()
                + ", limit: " + LIMIT_PER_HOUR
                + ", reset: " + bucket.getResetTime() + "}");
    }

    public static void main(String[] args) {

        String clientId = "abc123";

        System.out.println(checkRateLimit(clientId));
        System.out.println(checkRateLimit(clientId));
        System.out.println(checkRateLimit(clientId));

        getRateLimitStatus(clientId);
    }
}