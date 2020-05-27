package sample;

import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Objects;
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
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Album", "*.alb", "*.albprj"));
            ZipFile zipFile = new ZipFile(fileChooser.showOpenDialog(parent).getAbsolutePath());
            String tempPath = "/AlbumTemp";
            temp = new File(tempPath);
            boolean bool = temp.mkdir();
            if (bool) {
                System.out.println("Directory created successfully");
            } else {
                System.out.println("Sorry couldnt create specified directory");
            }

            FileInputStream fis;

           // Enumeration<? extends ZipEntry> entries = zipFile.entries();
            fis = new FileInputStream(zipFile.getName());
            ZipInputStream zipInputStream = new ZipInputStream(fis);

            ZipEntry entry = zipInputStream.getNextEntry();
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


            System.out.println();
            zipInputStream.closeEntry();
            zipInputStream.close();
            fis.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return images;
    }

    protected Vector<File> loadRecent(String filepath) {
        Vector<File> images = new Vector<>();
        try {


            ZipFile zipFile = new ZipFile(filepath);
            String tempPath = "/AlbumTemp";
            temp = new File(tempPath);
            boolean bool = temp.mkdir();
            if (bool) {
                System.out.println("Directory created successfully");
            } else {
                System.out.println("Sorry couldnt create specified directory");
            }

            FileInputStream fis;

            // Enumeration<? extends ZipEntry> entries = zipFile.entries();
            fis = new FileInputStream(zipFile.getName());
            ZipInputStream zipInputStream = new ZipInputStream(fis);

            ZipEntry entry = zipInputStream.getNextEntry();
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


            System.out.println();
            zipInputStream.closeEntry();
            zipInputStream.close();
            fis.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return images;
    }

    public Vector<File> open(Stage stage, String mode) {
        Vector<File> imageStack = new Vector<>();
        switch (mode) {
            case "multi":
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif", "*.jpeg"));
                imageStack.addAll(fileChooser.showOpenMultipleDialog(stage));
                return imageStack;
            case "folder":
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(stage);

                if (selectedDirectory != null) {
                    FilenameFilter filterJpg = (File dir, String name) -> name.toLowerCase().endsWith(".jpg");
                    FilenameFilter filterPng = (File dir, String name) -> name.toLowerCase().endsWith(".png");
                    FilenameFilter filterGif = (File dir, String name) -> name.toLowerCase().endsWith(".gif");

                    imageStack.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterJpg))));
                    imageStack.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterPng))));
                    imageStack.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterGif))));
                }
                return imageStack;
            default:
                return null;
        }


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
