package app;

import cli.CliCommand;
import cli.CliParser;
import codec.Codec;
import codec.HuffmanCodec;
import core.CodeTableGenerator;
import core.FrequencyAnalyzer;
import core.HuffmanTreeBuilder;
import exception.CompressionException;
import exception.InvalidFormatException;
import format.HuffFileFormat;
import model.CompressResult;
import model.DecompressResult;
import pipeline.CompressionPipeline;
import pipeline.DecompressionPipeline;

import java.io.IOException;

public class CompressionApp {

    public static void main(String[] args) {
        try {
            CliCommand command = CliParser.parse(args);
            if (command.getMode() == CliCommand.Mode.COMPRESS) {
                runCompress(command);
            } else {
                runDecompress(command);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("[Error] " + e.getMessage());
            printHelp();
            System.exit(1);
        } catch (InvalidFormatException e) {
            System.err.println("[Error] Unknown file : " + e.getMessage());
            System.exit(3);
        } catch (CompressionException e) {
            System.err.println("[Error] " + e.getMessage());
            System.exit(4);
        } catch (IOException e) {
            System.err.println("[Error]: " + e.getMessage());
            System.exit(2);
        }
    }

    private static void runCompress(CliCommand cmd) throws IOException {
        Codec codec = new HuffmanCodec();
        CompressionPipeline pipeline = new CompressionPipeline(new FrequencyAnalyzer(), new HuffmanTreeBuilder(), new CodeTableGenerator(), new HuffFileFormat(), codec);
        System.out.println("Compressing:" + cmd.getInputFilePath());
        CompressResult r = pipeline.compress(cmd);
        System.out.printf("%s%n",r.getOutputFile().getPath());
        System.out.printf("Original : %,d byte%n",r.getOriginalSize());
        System.out.printf("Compressd: %,d byte%n",r.getCompressedSize());
        System.out.printf("Ratio: %.1f%%%n",r.getCompressRatio());
        System.out.printf("Time :%d ms%n",r.getDurationMs());
    }

    private static void runDecompress(CliCommand cmd) throws IOException {
        DecompressionPipeline pipeline = new DecompressionPipeline(new HuffmanTreeBuilder(),new HuffFileFormat(),new HuffmanCodec());
        System.out.println("Opening:" + cmd.getInputFilePath());
        DecompressResult r = pipeline.decompress(cmd);
        System.out.printf("%s%n",r.getOutputFile().getPath());
        System.out.printf("Opened : %,d byte%n",r.getDecompressedSize());
        System.out.printf("Time:%d ms%n",r.getDurationMs());
    }

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("Compress: -c -i <file> [-o <output>][--chunk <MB>]");
        System.out.println("Open:-d -i <file> [-o <output>]");
    }
}