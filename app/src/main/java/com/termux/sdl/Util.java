package com.termux.sdl;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class Util {

    private static final String TAG = "Termux SDL JNI";

    public static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i=0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                              new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024 * 1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                String files[] = file.list();
                for (String temp: files) {
                    File fileDelete = new File(file, temp);
                    deleteDirectory(fileDelete);
                }
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            file.delete();
        }
    }


//    public int chmod(File path, int mode) throws Exception {
//        Class fileUtils = Class.forName("android.os.FileUtils");
//        Method setPermissions = fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
//        return (Integer) setPermissions.invoke(null, path.getAbsolutePath(), mode, -1, -1);
//    }


    public static boolean unpackZip(InputStream in, String to) {       
        ZipInputStream zipInput = null;
        try {
            OutputStream out = null;
            zipInput = new ZipInputStream(new BufferedInputStream(in));          
            ZipEntry entry = null;
            byte[] buffer = new byte[1024];

            while ((entry = zipInput.getNextEntry()) != null) {
                Log.i(TAG, "Unzipping file " + entry.getName());

                if (entry.isDirectory()) {
                    File file = new File(to + "/" + entry.getName());
                    file.mkdirs();
                    continue;
                }

                out = new FileOutputStream(to + "/" + entry.getName());

                for (int count = zipInput.read(buffer); count > 0; count = zipInput.read(buffer)) {
                    out.write(buffer, 0, count);
                }

                out.close();               
                zipInput.closeEntry();
            }
            zipInput.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
