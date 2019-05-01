package Modules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AccountFile {

    public AccountFile() {}

    public List<String> readFromFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return lines;
    }

    public void writeToFile(String filePath, String content) throws IOException {
        FileWriter write = new FileWriter(new File(filePath), true);
        PrintWriter writer = new PrintWriter(write);
        writer.printf(content + "\n");
        writer.close();
    }

    public void writeFullListToFile(String filePath, List<String> list) throws IOException {
        Files.write(Paths.get(filePath), list, Charset.defaultCharset());
    }
}
