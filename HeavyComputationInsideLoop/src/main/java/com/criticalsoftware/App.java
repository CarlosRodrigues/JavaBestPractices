package com.criticalsoftware;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class App {

    private static final int CLIENT_COUNT = 10000;

    public static void main(final String[] args) throws InterruptedException {
        List<String> clientNames = generateMockData(CLIENT_COUNT);

        System.out.println("Running regex inside loop");
        long start = System.nanoTime();
        int count = regexInsideLoop(clientNames, "smith");
        long end = System.nanoTime();
        long ellapsedInsideLoop = (end - start);
        System.out.println(String.format("Done. Found %d names and took %d nanoseconds", count, ellapsedInsideLoop));

        System.out.println("Running with regex patterm compiled outside loop");
        start = System.nanoTime();
        count = regexOutsideLoop(clientNames, "smith");
        end = System.nanoTime();
        long ellapsedOutsideLoop = (end - start);
        System.out.println(String.format("Done. Found %d names and took %d nanoseconds", count, ellapsedOutsideLoop));

        System.out.println(String.format("Reusing the compiled pattern was %.2f times faster. ",
                (float) ellapsedInsideLoop / ellapsedOutsideLoop));

    }

    static List<String> generateMockData(final int dataSetSize) {

        // inline for brevity
        String firstNameText = "Michael,Christopher,Jessica,Matthew,Ashley,Jennifer,Joshua,Amanda,Daniel,David,James,Robert,John,Joseph,Andrew,Ryan,Brandon,Jason,Justin,Sarah,William,Jonathan,Stephanie,Brian,Nicole,Nicholas,Anthony,Heather,Eric,Elizabeth,Adam,Megan,Melissa,Kevin,Steven,Thomas,Timothy,Christina,Kyle,Rachel,Laura,Lauren,Amber,Brittany,Danielle,Richard,Kimberly,Jeffrey,Amy,Crystal,Michelle,Tiffany,Jeremy,Benjamin,Mark,Emily,Aaron,Charles,Rebecca,Jacob,Stephen,Patrick,Sean,Erin,Zachary,Jamie,Kelly,Samantha,Nathan,Sara,Dustin,Paul,Angela";
        String lastNameText = "SMITH,JOHNSON,WILLIAMS,JONES,BROWN,DAVIS,MILLER,WILSON,MOORE,TAYLOR";

        String[] firstNames = firstNameText.split(",");
        String[] lastNames = lastNameText.split(",");

        final List<String> fullNames = new ArrayList<>();

        final Random randomGenerator = new Random();
        for (int i = 0; i < dataSetSize; i++) {
            final int firstNameIndex = randomGenerator.nextInt(firstNames.length);
            final int lastNameIndex = randomGenerator.nextInt(lastNames.length);
            fullNames.add(String.format("%s %s", firstNames[firstNameIndex], lastNames[lastNameIndex]));
        }

        return fullNames;
    }

    static int regexInsideLoop(List<String> clientNames, String lookupName) {
        int counter = 0;
        String regex = String.format("(?i).*%s.*", lookupName);

        for (String fullName : clientNames) {
            if (Pattern.matches(regex, fullName)) {
                counter++;
            }
        }

        return counter;
    }

    static int regexOutsideLoop(List<String> clientNames, String lookupName) {
        int counter = 0;
        String regex = String.format("(?i).*%s.*", lookupName);

        // pattern is compiled only once
        Pattern pattern = Pattern.compile(regex);

        for (String fullName : clientNames) {
            if (pattern.matcher(fullName).matches()) {
                counter++;
            }
        }

        return counter;
    }
}
