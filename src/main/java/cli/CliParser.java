package cli;

import java.io.File;

public class CliParser {

    public static CliCommand parse(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No arguments");
        }

        CliCommand.Mode mode = null;
        File inputFile = null;
        File outputFile = null;
        int chunkSizeMB = 32;

        for(int i = 0;i< args.length;i++){
            String arg = args[i];
            switch (arg){
                case "--compress":
                case "-c":
                    mode = CliCommand.Mode.COMPRESS;
                    break;
                case "--decompress":
                case "-d":
                    mode = CliCommand.Mode.DECOMPRESS;
                    break;
                case "--input":
                case "-i":
                    if (i+1< args.length){
                        inputFile = new File(args[++i]);

                    }else {
                        throw new IllegalArgumentException("There is file path needed after -i parameter ");
                    }
                    break;
                case "--output":
                case "-o":
                    if(i+1< args.length){
                        outputFile = new File(args[++i]);

                    }else{
                        throw new IllegalArgumentException("There is output path needed after -o parameter");
                    }
                    break;
                case "--chunk":
                    if(i+1< args.length){
                        try {
                            chunkSizeMB = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Chunk size must be number (MB)");
                        }
                    }else {
                        throw new IllegalArgumentException("There is number needed for chunk size (MB) ");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("An error occurred");
            }



        }
        if(mode == null){
            throw new IllegalArgumentException("Mode is not declared by user  --compress or --decompress ");

        }
        if(inputFile  == null){
            throw new IllegalArgumentException("The input path is not declared -");

        }
        if (outputFile == null) {
            outputFile = generateDefaultOutputFile(inputFile, mode);
        }

        return new CliCommand(inputFile.getAbsolutePath(),outputFile.getAbsolutePath(),mode,chunkSizeMB);
    }
    private static File generateDefaultOutputFile(File inputFile, CliCommand.Mode mode) {
        String path = inputFile.getPath();
        if (mode == CliCommand.Mode.COMPRESS) {
            return new File(path + ".huff");
        } else {
            if (path.endsWith(".huff")) {
                return new File(path.substring(0, path.length() - 5));
            }
            return new File(path +".decompressed");
        }
    }
}
