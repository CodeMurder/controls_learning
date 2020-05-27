package sample;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
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

    protected StatusBar overloadStack(TilePane root, Vector<File> images, StatusBar status) {
        return save(images, status, root.getScene(), "Album Project", "*.albprj");
    }

    protected StatusBar overloadStack(HBox root, Vector<File> images, StatusBar status) {
        return save(images, status, root.getScene(), "Album", "*.alb");
    }

    private StatusBar save(Vector<File> images, StatusBar status, Scene scene, String filter, String extension) {
        try {

            Stage parent = (Stage) scene.getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Album", extension));
            FileOutputStream fos = new FileOutputStream(fileChooser.showSaveDialog(parent).getAbsolutePath());
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

            fos.close();
            status.setText("Successfully saved)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


}