package sample;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class RecentProject {
    public static Vector<String> filePathList = new Vector<>();
    public File logPath = new File("recentlog.txt");
    String path;
    public static FileWriter fw;

    public RecentProject() throws IOException {

        if (logPath.createNewFile()) {

            System.out.println("created.");
        } else {
            System.out.println("already exist.");
        }
        fw = new FileWriter(logPath, true);
    }

    public String getFilepath(File file) {

        return file.getAbsolutePath();
    }

    public static void setFilepath(String filepath) throws IOException {
        filePathList.add(filepath);
        fw.write(filepath + "\n");
        fw.flush();
    }


    public static void closeStream() throws IOException {
        fw.close();
    }

    public void loadPathes() throws Exception {
        FileReader fr = new FileReader(logPath);
        Scanner iter = new Scanner(fr);
        while (iter.hasNextLine()) {
            filePathList.add(iter.nextLine());
        }
        fr.close();
    }


}
