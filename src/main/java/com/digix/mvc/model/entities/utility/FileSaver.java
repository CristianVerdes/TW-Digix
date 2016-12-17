package com.digix.mvc.model.entities.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class FileSaver implements Runnable {

    private String imageUrl;
    private String destinationFile;

    public FileSaver(String imageUrl, String destinationFile) {
        this.imageUrl = imageUrl;
        this.destinationFile = destinationFile;
    }

    @Override
    public void run() {
        File myFile = new File(destinationFile);
        if (!myFile.getParentFile().exists()) {
            myFile.getParentFile().mkdirs();
        }
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);


            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
