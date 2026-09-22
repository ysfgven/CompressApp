package model;

import java.io.File;

public class CompressResult {
    private final long originalSize;
    private final long compressedSize;
    private final double compressRatio;
    private final File outputFile;
    private final long durationMs;

    public CompressResult(long originalSize, long compressedSize,File outputFile, long durationMs) {
        this.originalSize = originalSize;
        this.compressedSize = compressedSize;
        this.compressRatio = originalSize > 0 ? (1.0 -(double)compressedSize/originalSize)*100.0:0.0;
        this.outputFile = outputFile;
        this.durationMs = durationMs;
    }

    public long getOriginalSize() {
        return originalSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public double getCompressRatio() {
        return compressRatio;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public long getDurationMs() {
        return durationMs;
    }

}
