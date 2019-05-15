/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubn.zipdownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 *
 * @author aojinadu
 */
public class DownloadFolder {
    public static void main(String[] args) {
         
        String zipFile = "C:\\HMOFiles\\HMO2.zip";
        String srcDir = "C:\\HMOFiles\\HMO1";
         
        try {
             
            // create byte buffer
            byte[] buffer = new byte[1024];
 
            FileOutputStream fos = new FileOutputStream(zipFile);
 
            ZipOutputStream zos = new ZipOutputStream(fos);
 
            File dir = new File(srcDir);
 
            File[] files = dir.listFiles();
 
            for (int i = 0; i < files.length; i++) {
                 
                System.out.println("Adding file: " + files[i].getName());
 
                FileInputStream fis = new FileInputStream(files[i]);
 
                // begin writing a new ZIP entry, positions the stream to the start of the entry data
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                 
                int length;
 
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
 
                zos.closeEntry();
 
                // close the InputStream
                fis.close();
            }
 
            // close the ZipOutputStream
            zos.close();
             
        }
        catch (IOException ioe) {
            System.out.println("Error creating zip file" + ioe);
        }
         
    }
}
