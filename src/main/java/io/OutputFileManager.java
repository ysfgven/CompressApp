package io;


import exception.CompressionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class OutputFileManager {
    private final File outputPath;
    private final File tempFile;

    public OutputFileManager(File outputPath) {
        this.outputPath = resolveConflict(outputPath);
        this.tempFile = new File(this.outputPath.getPath() + ".tmp");
    }

    public boolean canWrite() {
        File parentDir = outputPath.getParentFile();
        if (parentDir == null){
            parentDir = new File(".");
        }
        if (!parentDir.exists()){
            boolean created = parentDir.mkdirs();
            if (!created) {
                return false;
            }
        }
        return parentDir.canWrite();
    }
    public File createTempFile(){
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        }catch (IOException e) {
            throw new CompressionException("An error occurred while creating temp file or trying to delete the old temp file.",e);
        }
        return tempFile;

    }
    public void commit() {
        if (!tempFile.exists()) {
            return;
        }
        try {
            try{
                Files.move(tempFile.toPath(), outputPath.toPath(),StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }catch(AtomicMoveNotSupportedException e) {
                Files.move(tempFile.toPath(), outputPath.toPath(),StandardCopyOption.REPLACE_EXISTING);
            }
        }catch(IOException e) {
            rollback();
            throw new CompressionException("Output file couldnt finish", e);
        }
    }
    public void rollback(){
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }
    private File resolveConflict(File file){
        if(!file.exists()){
            return file;
        }
        String fullName = file.getName();
        String searchName = fullName;
        String ext = "";

        int lastDotIndex = fullName.lastIndexOf('.');
        if(lastDotIndex > 0){
            searchName = fullName.substring(0, lastDotIndex);
            ext = fullName.substring(lastDotIndex);
        }

        File parentDir = file.getParentFile();

        int count = 1;
        File newFile = file;

        while (newFile.exists()) {
            String newName = searchName + "_" + count + ext;
            newFile = new File(parentDir, newName);
            count++;
        }

        return newFile;
    }

    public File getOutputPath() {
        return outputPath;
    }
}
