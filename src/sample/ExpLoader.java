package sample;

import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ExpLoader {
    protected static File temp;
    protected static String state;

    protected Vector<File> load(TilePane root, Vector<File> images) {
        try {

            Stage parent = (Stage) root.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Album", "*.alb"));
            ZipFile zipFile = new ZipFile(fileChooser.showOpenDialog(parent).getAbsolutePath());
            String tempPath = "C:/AlbumTemp";
            temp = new File(tempPath);
            boolean bool = temp.mkdir();
            if (bool) {
                System.out.println("Directory created successfully");
            } else {
                System.out.println("Sorry couldnt create specified directory");
            }

            FileInputStream fis;

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            fis = new FileInputStream(zipFile.getName());
            ZipInputStream zipInputStream = new ZipInputStream(fis);

            ZipEntry entry = entries.nextElement();
            while (entry != null) {
                String fileName = entry.getName();
                File newFile = new File(temp.getAbsolutePath() + File.separator + fileName);
                state = newFile.getAbsolutePath();
                System.out.println(temp.getAbsolutePath() + File.separator + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.close();
                images.add(newFile);
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();

            }

            /*if (temp.isDirectory()) {
                for (File file : images) {
                    System.out.println("deleting" + file.getName());


                    System.out.println("Deleting Files. Success = " + file.delete());
                }
            }



            temp.deleteOnExit();


            System.out.println("Deleting Directory. Success = " + temp.delete());*/


            System.out.println();
            zipInputStream.closeEntry();
            zipInputStream.close();
            fis.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return images;
    }

    public static boolean deleteDirectory(File temp) {
        if (temp.isDirectory()) {
            File[] children = temp.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        } // either file or an empty directory
        System.out.println("removing file or directory : " + temp.getName());
        return temp.delete();
    }
}
