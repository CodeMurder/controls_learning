package sample;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

public class RecentProject {
    List<String> filePathList;
    String path;

    public String getFilepath(File file) {

        return file.getAbsolutePath();
    }

    public String addFilepath(String filepath) throws Exception {
        this.filePathList.add(filepath);
        savePathes();
        return filepath;
    }

    public void savePathes() throws Exception {
        FileWriter fw = new FileWriter("C:/recent.txt");
        for (String s : filePathList) {
            fw.write(s + "\n");
        }
        fw.close();

    }

    public void loadPathes() throws Exception {
        FileReader fr = new FileReader("C:/recent.txt");
        Scanner iter = new Scanner(fr);
        while (iter.hasNextLine()) {
            filePathList.add(iter.nextLine());
        }
        fr.close();
    }


}
