package pipeline;

import bitio.BitWriter;
import cli.CliCommand;
import codec.Codec;
import codec.HuffmanCodec;
import core.CodeTableGenerator;
import core.FrequencyAnalyzer;
import core.HuffmanNode;
import core.HuffmanTreeBuilder;
import format.FileHeader;
import format.HuffFileFormat;
import io.FileChunker;
import io.OutputFileManager;
import model.CodeTable;
import model.CompressResult;

import java.io.*;
import java.util.Map;

public class CompressionPipeline {
    private final FrequencyAnalyzer frequencyAnalyzer;
    private final HuffmanTreeBuilder treeBuilder;
    private final CodeTableGenerator codeTableGenerator;
    private final HuffFileFormat fileFormat;
    private final Codec codec;


    public CompressionPipeline(FrequencyAnalyzer frequencyAnalyzer, HuffmanTreeBuilder treeBuilder, CodeTableGenerator codeTableGenerator, HuffFileFormat fileFormat, Codec codec) {
        this.frequencyAnalyzer = frequencyAnalyzer;
        this.treeBuilder = treeBuilder;
        this.codeTableGenerator = codeTableGenerator;
        this.fileFormat = fileFormat;
        this.codec = codec;
    }

    public CompressResult compress(CliCommand command) throws IOException {
        long startTime = System.currentTimeMillis();
        File inputFile = new File(command.getInputFilePath());
        int chunkSize = command.getChunkSizeMB()*1024*1024;
        Map<Byte,Integer> freqTable;

        try (FileInputStream fis = new FileInputStream(inputFile)) {
            freqTable = frequencyAnalyzer.analyze(fis);
        }
        HuffmanNode root= treeBuilder.build(freqTable);
        CodeTable codeTable = codeTableGenerator.generate(root);
        OutputFileManager fileManager = new OutputFileManager(new File(command.getOutputFilePath()));

        if(!fileManager.canWrite()) {
            throw new IOException("Output path is not writable: " + command.getOutputFilePath());
        }

        File tempFile = fileManager.createTempFile();
        FileHeader header = new FileHeader(inputFile.getName(),inputFile.length(), freqTable);
        int paddingBits;
        long compressedDataSize;

        try (RandomAccessFile raf = new RandomAccessFile(tempFile,"rw")) {
            fileFormat.writeHeader(header, new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    raf.write(b);
                }
                @Override
                public void write(byte[] b, int o, int l) throws IOException {
                    raf.write(b,o,l);
                }
            });
            long dataStart = raf.getFilePointer();
            OutputStream rafStream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    raf.write(b);
                }
                @Override
                public void write(byte[] b, int o, int l) throws IOException {
                    raf.write(b,o,l);
                }
            };
            BufferedOutputStream buffered = new BufferedOutputStream(rafStream, 64*1024);
            BitWriter bw = new BitWriter(buffered);
            for (byte[] chunk : new FileChunker(inputFile,chunkSize)) {
                codec.encodeChunk(chunk, codeTable, bw);
            }
            paddingBits = bw.flush();
            buffered.flush();
            compressedDataSize = raf.getFilePointer() - dataStart;

            raf.seek(header.getCompressedDataSizeOffset());
            raf.writeLong(compressedDataSize);

            raf.seek(header.getPaddingBitsOffset());
            raf.writeByte(paddingBits);
        }
        fileManager.commit();
        File out = fileManager.getOutputPath();
        return new CompressResult(inputFile.length(), out.length(), out,System.currentTimeMillis()-startTime);
    }
}
