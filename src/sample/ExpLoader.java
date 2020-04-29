package sample;

import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ExpLoader {
    static Vector<File> load(TilePane root, Vector<File> images, StatusBar status) {
        try {

            Stage parent = (Stage) root.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Album", "*.alb"));
            ZipFile zipFile = new ZipFile(fileChooser.showOpenDialog(parent).getAbsolutePath());

            FileInputStream fis;

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            fis = new FileInputStream(zipFile.getName());
            ZipInputStream zipInputStream = new ZipInputStream(fis);

            ZipEntry entry = entries.nextElement();
            while (entry != null) {
                String fileName = entry.getName();
                File newFile = new File(fileName);
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


            zipInputStream.closeEntry();
            zipInputStream.close();
            fis.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return images;
    }

}
