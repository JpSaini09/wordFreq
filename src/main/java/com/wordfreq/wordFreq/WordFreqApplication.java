package com.wordfreq.wordFreq;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SpringBootApplication
public class WordFreqApplication {

    public static void main(String[] args) {
        //taking file path from resources as mock path of ./src/main/resources/frequency.txt
        String path = args[0];
        int N = Integer.parseInt(args[1]);

        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            //reserve sorting the map according to the words(key).
            TreeMap<String, Integer> map = new TreeMap<>((o1, o2) -> {
                if (o1.compareTo(o2) > 0) {
                    return -1;
                } else if (o1.compareTo(o2) < 0) {
                    return 1;
                }
                return 0;
            });

            String line;
            while ((line = br.readLine()) != null) {

                //splitting the line to words and replacing the special char
                String[] words = line.trim()
                        .replaceAll("[-+.^:,\"(){}]", "")
                        .replace("’", "") //taking in consideration mac char colon
                        .split(" ");

                for (String word : words) {
                    //checking for only alpha words evicting alphanumeric and numeric words
                    if (word.chars().allMatch(ch -> Character.isLetter(ch) || ch == '\'')) {
                        String key = word.toLowerCase();
                        map.put(key, map.containsKey(key) ? map.get(key) + 1 : 1);
                    }
                }
            }

            System.out.println("Most frequent");
            String maxFreq = map.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(N)
                    .map(entry -> String.format("(%s:%s)", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(","));
            System.out.println(maxFreq);

            System.out.println("Least frequent");
            String leastFreq = map.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .sorted(Map.Entry.comparingByValue())
                    .limit(N)
                    .map(entry -> String.format("(%s:%s)", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(","));
            System.out.println(leastFreq);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
