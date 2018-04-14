package edu.iau.abjad.AbjadApp;
public class LevenshteinDistance {
    static int globalCost;
    public static double computeEditDistance(String s1, String s2) {
        double similarityOfStrings = 0.0;
        int editDistance = 0;
        if (s1.length() < s2.length()) { // s1 should always be bigger
            String swap = s1;
            s1 = s2;
            s2 = swap;
        }
       int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                }
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }

        System.out.println("cost: "+costs[s2.length()]);
        globalCost= costs[s2.length()];
        int bigLen = s1.length();
        editDistance = costs[s2.length()];
        if (bigLen == 0) {
            similarityOfStrings = 1.0; /* both strings are zero length */
        } else {
            similarityOfStrings = (bigLen - editDistance) / (double) bigLen;
        }
        System.out.println("(" + similarityOfStrings + ")");
        return  similarityOfStrings;
    }
}
