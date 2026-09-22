package restaurant.util;

import java.io.*;
import java.util.*;

/**
 * Small helper for reading and writing CSV-style data files.
 * Every record is one line; fields are separated by the '|' character
 * (chosen instead of ',' so ordinary text fields can contain commas safely).
 */
public class FileUtil {

    private static final String DATA_DIR = "data";

    /** Makes sure the data directory exists. */
    public static void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String path(String fileName) {
        return DATA_DIR + File.separator + fileName;
    }

    /** Reads every line of a file. Returns an empty list if the file does not exist yet. */
    public static List<String> readLines(String fileName) {
        ensureDataDir();
        File file = new File(path(fileName));
        List<String> lines = new ArrayList<>();
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        }
        return lines;
    }

    /** Overwrites the file with the given lines (used after every add/update/delete). */
    public static void writeLines(String fileName, List<String> lines) {
        ensureDataDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path(fileName), false))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing " + fileName + ": " + e.getMessage());
        }
    }

    public static String[] split(String line) {
        // -1 keeps trailing empty fields (e.g. an empty "paidDate")
        return line.split("\\|", -1);
    }

    public static String join(String... fields) {
        return String.join("|", fields);
    }
}
