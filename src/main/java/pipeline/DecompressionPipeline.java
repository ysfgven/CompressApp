package pipeline;

import cli.CliCommand;
import codec.Codec;
import codec.HuffmanCodec;
import core.HuffmanNode;
import core.HuffmanTreeBuilder;
import format.FileHeader;
import format.HuffFileFormat;
import io.OutputFileManager;
import model.DecompressResult;

import java.io.*;

public class DecompressionPipeline {
    private final HuffmanTreeBuilder treeBuilder;
    private final HuffFileFormat format;
    private final Codec codec;

    public DecompressionPipeline(HuffmanTreeBuilder treeBuilder, HuffFileFormat format, Codec codec) {
        this.treeBuilder = treeBuilder;
        this.format = format;
        this.codec = codec;
    }

    public DecompressResult decompress(CliCommand command) throws IOException {
        long startTime = System.currentTimeMillis();
        File inputFile = new File(command.getInputFilePath());

        OutputFileManager fileManager = new OutputFileManager(new File(command.getOutputFilePath()));
        if (!fileManager.canWrite()) {
            throw new IOException("Output path is not writable: " + command.getOutputFilePath());
        }
        File tempFile = fileManager.createTempFile();

        try (FileInputStream  fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis)) {
            FileHeader header = format.readHeader(bis);
            HuffmanNode root = treeBuilder.build(header.getFrequencyTable());

            try(FileOutputStream fos = new FileOutputStream(tempFile);BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                codec.decompress(bis, bos,root, header.getPaddingBits(),header.getCompressedDataSize());
            }
        }
        fileManager.commit();
        File out = fileManager.getOutputPath();

        return new DecompressResult(inputFile.length(), out.length(), out,System.currentTimeMillis() - startTime);
    }


}
