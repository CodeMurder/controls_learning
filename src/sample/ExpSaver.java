package sample;

import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExpSaver {

    protected StatusBar save(TilePane root, Vector<File> images, StatusBar status) {
        try {
            String filepath;
            Stage parent = (Stage) root.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Album", "*.alb"));
            filepath = fileChooser.showSaveDialog(parent).getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(filepath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);

            for (File srcFile : images) {
                File fileToZip = new File(String.valueOf(srcFile));
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            RecentProject.setFilepath(filepath);
            fos.close();
            status.setText("Successfully saved)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


}