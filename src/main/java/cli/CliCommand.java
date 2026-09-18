package cli;

public class CliCommand {

    private String inputFilePath;
    private String outputFilePath;
    private Mode mode;
    private int chunkSizeMB;

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
}
