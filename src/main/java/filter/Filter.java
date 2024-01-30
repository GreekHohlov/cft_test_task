package filter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Filter {
    private String writePath = "";
    private String namePrefix = "";
    private String writeType = "";
    private String statisticType = "";
    private Long maxInteger = null;
    private Long minInteger = null;
    private Double maxDouble = null;
    private Double minDouble = null;
    private Integer maxStringLength = null;
    private Integer minStringLength = null;
    private List<String> stringList = new ArrayList<>();
    private List<Long> integerList = new ArrayList<>();
    private List<Double> doubleList = new ArrayList<>();
    public void setParameters(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o") && !args[i + 1].startsWith("-")) {
                if (args[i + 1].endsWith("/")) {
                    writePath = args[i + 1];
                } else {
                    writePath = args[i + 1] + "/";
                }
                i++;
            }
            if (args[i].equals("-p") && !args[i + 1].startsWith("-")) {
                namePrefix = args[i + 1];
                i++;
            }
            if (args[i].equals("-s") || args[i].equals("-f")) {
                statisticType = args[i];
            }
            if (args[i].equals("-a")) {
                writeType = args[i];
            }
            if (args[i].endsWith(".txt")) {
                readFile(args[i]);
            }
        }
        writeFiles();
    }
    private void readFile(String file) {
        String tempLine;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader buffer = new BufferedReader(fileReader)) {
            while (buffer.ready()) {
                tempLine = buffer.readLine();
                try {
                    integerList.add(Long.valueOf(tempLine));
                } catch (IllegalArgumentException eInt) {
                    try {
                        doubleList.add(Double.valueOf(tempLine));
                    } catch (IllegalArgumentException eDouble) {
                        stringList.add(tempLine);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File not found: " + file);
        }
    }
    private void writeFiles() {
        if (!new File(writePath).exists()) {
            new File(writePath).mkdirs();
        }
        if (!integerList.isEmpty()) {
            try (FileWriter fileWriter = new FileWriter(writePath + namePrefix + "integers.txt", (!writeType.isEmpty()))) {
                Long sum = 0L;
                for (Long element: integerList) {
                    if (!(maxInteger == null)) {
                        maxInteger = Math.max(maxInteger, element);
                        minInteger = Math.min(minInteger, element);
                    } else {
                        maxInteger = element;
                        minInteger = element;
                    }
                    sum += element;
                    fileWriter.write(element + "\n");
                }
                printStatistic(sum);
            } catch (IOException e) {
                System.out.println("File not created: " +  writePath + namePrefix + "integers.txt");
            }
        }
        if (!doubleList.isEmpty()) {
            try (FileWriter fileWriter = new FileWriter(writePath + namePrefix + "floats.txt", (!writeType.isEmpty()))){
                Double sum = 0d;
                for (Double element: doubleList) {
                    if (!(maxDouble == null)) {
                        maxDouble = Math.max(maxDouble, element);
                        minDouble = Math.min(minDouble, element);
                    } else {
                        maxDouble = element;
                        minDouble = element;
                    }
                    sum += element;
                    fileWriter.write(element + "\n");
                }
                printStatistic(sum);
            } catch (IOException e) {
                System.out.println("File not created: " +  writePath + namePrefix + "floats.txt");
            }
        }
        if (!stringList.isEmpty()) {
            try (FileWriter fileWriter = new FileWriter(writePath + namePrefix + "strings.txt", (!writeType.isEmpty()))){
                for (String element: stringList) {
                    if (!(maxStringLength == null)) {
                        maxStringLength = Math.max(maxStringLength, element.length());
                        minStringLength = Math.min(minStringLength, element.length());
                    } else {
                        maxStringLength = element.length();
                        minStringLength = element.length();
                    }
                    fileWriter.write(element + "\n");
                }
                printStatistic();
            } catch (IOException e) {
                System.out.println("File not created: " +  writePath + namePrefix + "strings.txt");
            }
        }
    }
    private void printStatistic() {
        System.out.println("String elements count: " + stringList.size());
        if (statisticType.equals("-f")) {
            System.out.println("Minimum string length: " + minStringLength);
            System.out.println("Maximum string length: " + maxStringLength);
        }
    }
    private void printStatistic(Long sum) {
        System.out.println("Integer element count: " + integerList.size());
        if (statisticType.equals("-f")) {
            System.out.println("Minimum integer value: " + minInteger);
            System.out.println("Maximum integer value: " + maxInteger);
            System.out.println("Sum integer elements: " + sum);
            System.out.println("Integer average: " + sum * 1.0 / integerList.size());
        }
    }
    private void printStatistic(Double sum) {
        System.out.println("Float elements count: " + doubleList.size());
        if (statisticType.equals("-f")) {
            System.out.println("Minimum float value: " + minDouble);
            System.out.println("Maximum float value: " + maxDouble);
            System.out.println("Sum float elements: " + sum);
            System.out.println("Float average: " + sum / doubleList.size());
        }
    }
}
