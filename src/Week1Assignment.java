import java.util.*;

public class Week1Assignment {

    static HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    static int N = 5; // 5-gram window

    // Extract n-grams
    public static List<String> extractNgrams(String text) {

        List<String> ngrams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }


    public static void indexDocument(String documentId, String text) {

        List<String> ngrams = extractNgrams(text);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(documentId);
        }

        System.out.println(documentId + " indexed with " + ngrams.size() + " n-grams");
    }


    public static void analyzeDocument(String documentId, String text) {

        List<String> ngrams = extractNgrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String doc : ngramIndex.get(gram)) {

                    if (!doc.equals(documentId)) {

                        matchCount.put(doc,
                                matchCount.getOrDefault(doc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity =
                    ((double) matches / ngrams.size()) * 100;

            System.out.println("Found " + matches +
                    " matching n-grams with " + doc);

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 50) {

                System.out.println("⚠ PLAGIARISM DETECTED");
            }
        }
    }

    public static void main(String[] args) {

        String essay1 = "machine learning algorithms improve data analysis and prediction models significantly";
        String essay2 = "machine learning algorithms improve data analysis and prediction accuracy significantly";
        String essay3 = "sports events bring excitement and entertainment to millions of fans worldwide";

        indexDocument("essay_089.txt", essay1);
        indexDocument("essay_092.txt", essay2);

        analyzeDocument("essay_123.txt", essay1);

    }
}