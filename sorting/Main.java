package sorting;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(final String[] args) {

        String sortType = "natural";
        String dataType = "word";
        String inputFile = "";
        String outputFile = "";

        for (int i = 0; i < args.length;) {
            switch (args[i]) {
                case "-sortingType":
                    sortType = "";
                    for (i++; i < args.length && !args[i].startsWith("-"); i++) {
                        sortType = args[i];
                    }
                    break;
                case "-dataType":
                    dataType = "word";
                    for (i++; i < args.length && !args[i].startsWith("-"); i++) {
                        dataType = args[i];
                    }
                    break;
                case "-inputFile":
                    for (i++; i < args.length && !args[i].startsWith("-"); i++) {
                        inputFile = args[i];
                    }
                    break;
                case "-outputFile":
                    for (i++; i < args.length && !args[i].startsWith("-"); i++) {
                        outputFile = args[i];
                    }
                    break;
                    
                default:
                    System.out.println(args[i] + " isn't a valid parameter. It's skipped.");
                    i++;
            }
        }

        if ("".equals(sortType)) {
            System.out.println("No sorting type defined!");
            return;
        }
        if ("".equals(dataType)) {
            System.out.println("No data type defined!");
            return;
        }

        ArrayList<String> list = new ArrayList<>();

        if (!"".equals(inputFile)) {
            try {
                File file = new File(inputFile);
                BufferedReader reader = new BufferedReader(new FileReader(file)); 
                String rec;
                while((rec = reader.readLine()) != null) {
                    list.add(rec);
                } 
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
            scanner.close();       
        }     
        
        Action action = new Action(list);
 
        switch (sortType) {
            case "natural":
                switch (dataType) {
                    case "long":
                        action.sortNumbers();
                        break;
                    case "word":
                        action.sortWords();
                        break;
                    case "line":
                        action.sortLines();
                        break;
                }
                break;
            case "byCount":
                switch (dataType) {
                    case "long":
                        action.countNumbers();
                        break;
                    case "word":
                        action.countWords();
                        break;
                    case "line":
                        action.countLines();
                    break;
            }
            break;
        }

        if ("".equals(outputFile)) {
            action.print();
        } else {
            action.print(outputFile);
        }
    }
}

class Action {

    ArrayList<String> list;
    String output;

    Action(ArrayList<String> list) {
        this.list = list;        
    }

    void sortNumbers() {

        long[] array = getLongArray();
        int n = array.length;

        Sort sort = new Sort("number");
        sort.mergeSort(array, 0, n);

        output = String.format("Total numbers: %d", n) + "\n";
        output += "Sorted data: ";
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                output += " ";
            }
            output += array[i];
        }
        output += "\n";
    }

    void sortWords() {

        String[] array = getWordArray();
        int n = array.length;

        Sort sort = new Sort("word");
        sort.mergeSort(array, 0, n);

        output = String.format("Total words: %d", n) + "\n";
        output += "Sorted data: ";
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                output += " ";
            }
            output += array[i];
        }
        output += "\n";
    }

    void sortLines() {
   
        String[] array = getLineArray();
        int n = array.length;

        Sort sort = new Sort("line");
        sort.mergeSort(array, 0, n);

        output = String.format("Total lines: %d", n) + "\n";
        output += "Sorted data: ";
        for (int i = 0; i < n; i++) {
            output += "\n";
            output += array[i];
        }
        output += "\n";
    }

    void countNumbers() {

        long[] longArray = getLongArray();
        int n = longArray.length;

        Count count = new Count("number");
        Map<Long, Integer> map = count.frequency(longArray);

        LongFrequecy[] array = new LongFrequecy[map.size()];

        int i = 0;
        for (var entry : map.entrySet()) {
            array[i++] = new LongFrequecy(entry.getKey(), entry.getValue());
        }
       
        Sort sort = new Sort("long");
        sort.mergeSort(array, 0, map.size());

        output = String.format("Total numbers: %d.", n) + "\n";
        for (var f : array) {
            int ratio = (f.frequency * 1000 / n + 5) / 10;
            output += String.format("%d: %d time(s), %d%%", f.key, f.frequency, ratio) + "\n";
        }
    }

    void countWords() {
        String[] wordArray = getWordArray();
        int n = wordArray.length;

        Count count = new Count("word");
        Map<String, Integer> map = count.frequency(wordArray);

        StringFrequecy[] array = new StringFrequecy[map.size()];

        int i = 0;
        for (var entry : map.entrySet()) {
            array[i++] = new StringFrequecy(entry.getKey(), entry.getValue());
        }
       
        Sort sort = new Sort("word");
        sort.mergeSort(array, 0, map.size());

        output = String.format("Total words: %d.", n) + "\n";
        for (var f : array) {
            int ratio = (f.frequency * 1000 / n + 5) / 10;
            output += String.format("%s: %d time(s), %d%%", f.key, f.frequency, ratio) + "\n";
        }
    }

    void countLines() {
        String[] lineArray = getLineArray();
        int n = lineArray.length;

        Count count = new Count("line");
        Map<String, Integer> map = count.frequency(lineArray);

        StringFrequecy[] array = new StringFrequecy[map.size()];

        int i = 0;
        for (var entry : map.entrySet()) {
            array[i++] = new StringFrequecy(entry.getKey(), entry.getValue());
        }
       
        Sort sort = new Sort("line");
        sort.mergeSort(array, 0, map.size());

        output = String.format("Total lines: %d.", n) + "\n";
        for (var f : array) {
            int ratio = (f.frequency * 1000 / n + 5) / 10;
            output += String.format("%s: %d time(s), %d%%", f.key, f.frequency, ratio) + "\n";
        }
    }

    long[] getLongArray() {
        ArrayList<Long> numbers = new ArrayList<>();
        for (String line: list) {
            String[] words = line.split("[ ]+");
            for (String word: words) {
                if (!word.matches("[-]?[0-9]+")) {
                    System.out.println(word + " isn't a long. It's skipped.");
                    continue;
                }
                long number = Long.parseLong(word);
                numbers.add(number);
            }
        }

        int n = numbers.size();        
        long[] array = new long[n];
        int i = 0;
        for (long number: numbers) {
            array[i++] = number;
        }

        return array;
    }

    String[] getWordArray() {
        ArrayList<String> words = new ArrayList<>();
        for (String line: list) {
            String[] strs = line.split("[ ]+");
            for (String word: strs) {
                words.add(word);
            }
        }

        int n = words.size();        
        String[] array = new String[n];
        int i = 0;
        for (String word: words) {
            array[i++] = word;
        }

        return array;
    }

    String[] getLineArray() {

        int n = list.size();        
        String[] array = new String[n];
        int i = 0;
        for (String line: list) {
            array[i++] = line;
        }

        return array;
    }

    void print() {
        System.out.print(output);
    }
   
    void print(String outputFile) {
        try {
            File file = new File(outputFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); 
            writer.write(output, 0, output.length());
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

class LongFrequecy {

    long key;
    int frequency;

    LongFrequecy(long key, int frequency) {
        this.key = key;
        this.frequency = frequency;
    }
}

class StringFrequecy {

    String key;
    int frequency;

    StringFrequecy(String key, int frequency) {
        this.key = key;
        this.frequency = frequency;
    }
}

class Count {

    String type;

    Count(String type) {
        this.type = type;
    }

    Map<Long, Integer> frequency (long[] array) {
        Map<Long, Integer> map = new TreeMap<>();

        for (long number: array) {
            if (map.containsKey(number)) {
                int count = map.get(number) + 1;
                map.put(number, count); 
            } else {
                map.put(number, 1);
            }   
        }

        return map;
    }

    Map<String, Integer> frequency (String[] array) {
        Map<String, Integer> map = new TreeMap<>();

        for (String key: array) {
            if (map.containsKey(key)) {
                int count = map.get(key) + 1;
                map.put(key, count); 
            } else {
                map.put(key, 1);
            }   
        }
        
        return map;
    }

}

class Sort {

    String type;

    Sort(String type) {
        this.type = type;
    }
 
    void mergeSort(LongFrequecy[] array, int leftIncl, int rightExcl) {
        if (rightExcl <= leftIncl + 1) {
            return;
        }
    
        int middle = leftIncl + (rightExcl - leftIncl) / 2;
        mergeSort(array, leftIncl, middle);  
        mergeSort(array, middle, rightExcl); 
        merge(array, leftIncl, middle, rightExcl);
    }
 
    void mergeSort(StringFrequecy[] array, int leftIncl, int rightExcl) {
        if (rightExcl <= leftIncl + 1) {
            return;
        }
    
        int middle = leftIncl + (rightExcl - leftIncl) / 2;
        mergeSort(array, leftIncl, middle);  
        mergeSort(array, middle, rightExcl); 
        merge(array, leftIncl, middle, rightExcl);
    }

    void mergeSort(int[] array, int leftIncl, int rightExcl) {
        if (rightExcl <= leftIncl + 1) {
            return;
        }
    
        int middle = leftIncl + (rightExcl - leftIncl) / 2;
        mergeSort(array, leftIncl, middle);  
        mergeSort(array, middle, rightExcl); 
        merge(array, leftIncl, middle, rightExcl);
    }

    void mergeSort(long[] array, int leftIncl, int rightExcl) {
        if (rightExcl <= leftIncl + 1) {
            return;
        }
    
        int middle = leftIncl + (rightExcl - leftIncl) / 2;
        mergeSort(array, leftIncl, middle);  
        mergeSort(array, middle, rightExcl); 
        merge(array, leftIncl, middle, rightExcl);
    }

    void mergeSort(String[] array, int leftIncl, int rightExcl) {
        if (rightExcl <= leftIncl + 1) {
            return;
        }
    
        int middle = leftIncl + (rightExcl - leftIncl) / 2;
        mergeSort(array, leftIncl, middle);  
        mergeSort(array, middle, rightExcl); 
        merge(array, leftIncl, middle, rightExcl);
    }
    
    void merge(StringFrequecy[] array, int left, int middle, int right) {

        int i = left;   
        int j = middle; 
        int k = 0;      
            
        StringFrequecy[] temp = new StringFrequecy[right - left]; 
    
        while (i < middle && j < right) {
            if (array[i].frequency <= array[j].frequency) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }
    
        for (;i < middle; i++, k++) {
            temp[k] = array[i];
        }
    
        for (;j < right; j++, k++) {
            temp[k] = array[j];
        }
    
        System.arraycopy(temp, 0, array, left, temp.length);
    }
    
    void merge(LongFrequecy[] array, int left, int middle, int right) {

        int i = left;   
        int j = middle; 
        int k = 0;      
            
        LongFrequecy[] temp = new LongFrequecy[right - left]; 
    
        while (i < middle && j < right) {
            if (array[i].frequency <= array[j].frequency) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }
    
        for (;i < middle; i++, k++) {
            temp[k] = array[i];
        }
    
        for (;j < right; j++, k++) {
            temp[k] = array[j];
        }
    
        System.arraycopy(temp, 0, array, left, temp.length);
    }

    void merge(int[] array, int left, int middle, int right) {

        int i = left;   
        int j = middle; 
        int k = 0;      
            
        int[] temp = new int[right - left]; 
    
        while (i < middle && j < right) {
            if (array[i] <= array[j]) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }
    
        for (;i < middle; i++, k++) {
            temp[k] = array[i];
        }
    
        for (;j < right; j++, k++) {
            temp[k] = array[j];
        }
    
        System.arraycopy(temp, 0, array, left, temp.length);
    }

    void merge(long[] array, int left, int middle, int right) {

        int i = left;   
        int j = middle; 
        int k = 0;      
            
        long[] temp = new long[right - left]; 
    
        while (i < middle && j < right) {
            if (array[i] <= array[j]) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }
    
        for (;i < middle; i++, k++) {
            temp[k] = array[i];
        }
    
        for (;j < right; j++, k++) {
            temp[k] = array[j];
        }
    
        System.arraycopy(temp, 0, array, left, temp.length);
    }

    void merge(String[] array, int left, int middle, int right) {

        int i = left;   
        int j = middle; 
        int k = 0;      
            
        String[] temp = new String[right - left]; 
    
        while (i < middle && j < right) {
            if (array[i].compareTo(array[j]) <= 0) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }
    
        for (;i < middle; i++, k++) {
            temp[k] = array[i];
        }
    
        for (;j < right; j++, k++) {
            temp[k] = array[j];
        }
    
        System.arraycopy(temp, 0, array, left, temp.length);
    }
   
    public static void mergeDescendant(int[] array, int left, int middle, int right) {

        int i = left;   
        int j = middle; 
        int k = 0;      
            
        int[] temp = new int[right - left]; 
    
        while (i < middle && j < right) {
            if (array[i] >= array[j]) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }
    
        for (;i < middle; i++, k++) {
            temp[k] = array[i];
        }
    
        for (;j < right; j++, k++) {
            temp[k] = array[j];
        }
    
        System.arraycopy(temp, 0, array, left, temp.length);
    }

}
