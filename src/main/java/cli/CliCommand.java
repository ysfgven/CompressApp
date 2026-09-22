package cli;

public class CliCommand {

    private final String inputFilePath;
    private final String outputFilePath;
    private final Mode mode;
    private final int chunkSizeMB;

    public enum Mode {
        COMPRESS,
        DECOMPRESS
    }

    public CliCommand(String inputFilePath, String outputFilePath, Mode mode, int chunkSizeMB) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.mode = mode;
        this.chunkSizeMB = chunkSizeMB;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public Mode getMode() {
        return mode;
    }

    public int getChunkSizeMB() {
        return chunkSizeMB;
    }
}
