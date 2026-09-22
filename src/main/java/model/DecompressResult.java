package model;

import java.io.File;

public class DecompressResult {
     private final long compressedSize;
     private final long decompressedSize;
     private final File outputFile;
     private final long durationMs;

     public DecompressResult(long compressedSize, long decompressedSize, File outputFile, long durationMs) {
          this.compressedSize = compressedSize;
          this.decompressedSize = decompressedSize;
          this.outputFile = outputFile;
          this.durationMs = durationMs;

     }
     public long getDecompressedSize() {
          return decompressedSize;
     }

     public File getOutputFile() {
          return outputFile;
     }

     public long getDurationMs() {
          return durationMs;
     }
}
