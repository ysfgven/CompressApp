package model;

import java.io.File;

public class CompressResult {
    private long originalSize;
    private long compressedSize;
    private double compressRatio;
    private File outputFile;
    private long durationMs;

    public long getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(long originalSize) {
        this.originalSize = originalSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public double getCompressRatio() {
        return compressRatio;
    }

    public void setCompressRatio(double compressRatio) {
        this.compressRatio = compressRatio;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }
}
