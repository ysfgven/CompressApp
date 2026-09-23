# Huffman File Compressor

A streaming, chunk-based file compressor and decompressor using Huffman coding, written in Java.

## Features


- Lossless compression via Huffman coding

- Streaming architecture — large files processed in configurable chunks (default 32 MB), not loaded fully into memory

- Custom binary file format (`.huff`) with magic number, version, and embedded frequency table for self-contained decompression

- Atomic output — writes to a temp file first, commits only on success; rolls back on failure

- Conflict-safe output — auto-renames if the output file already exists (`file_1.huff`, `file_2.huff`, …)


---

## Project Structure

```text
src/
├── CompressionApp.java          # Entry point & composition root
│
├── bitio/
│   ├── BitWriter.java           # Writes individual bits to an OutputStream
│   └── BitReader.java           # Reads individual bits from an InputStream
│
├── cli/
│   ├── CliCommand.java          # Parsed command value object
│   └── CliParser.java           # Argument parser
│
├── codec/
│   ├── Codec.java               # Compression/decompression interface
│   └── HuffmanCodec.java        # Codec implementation
│
├── core/
│   ├── FrequencyAnalyzer.java   # Byte frequency counting
│   ├── HuffmanTreeBuilder.java  # Builds Huffman tree from frequency table
│   ├── CodeTableGenerator.java  # DFS tree walk → CodeTable
│   ├── HuffmanEncoder.java      # Encodes byte[] chunks via BitWriter
│   ├── HuffmanDecoder.java      # Decodes bit stream via BitReader
│   └── HuffmanNode.java         # Tree node
│
├── exception/
│   ├── CompressionException.java
│   ├── DecompressionException.java
│   └── InvalidFormatException.java
│
├── format/
│   ├── FileHeader.java          # Header data + byte offset helpers
│   └── HuffFileFormat.java      # Pure serializer: writeHeader / readHeader
│
├── io/
│   ├── FileChunker.java         # Iterable<byte[]> over a file
│   └── OutputFileManager.java   # Temp file, commit, rollback, conflict resolution
│
├── model/
│   ├── CodeTable.java           # Byte → bit-string mapping
│   ├── CompressResult.java      # Compression result stats
│   └── DecompressResult.java    # Decompression result stats
│
└── pipeline/
    ├── CompressionPipeline.java    # Two-pass compression workflow
    └── DecompressionPipeline.java  # Decompression workflow

---

## .huff File Format

`Offset        Size   Field
──────────────────────────────────────────────────────
0             4      Magic number: 0x48 0x55 0x46 0x46  ("HUFF")
4             2      Version (short): 1
6             8      Original file size (long)
14            4      File name length N (int, UTF-8 byte count)
18            N      Original file name (UTF-8)
18+N          8      Compressed data size in bytes (long)  ← patched after encoding
26+N          1      Padding bits in last byte (0–7)       ← patched after encoding
27+N          4      Frequency table entry count M (int)
31+N          5×M    Frequency table: 1 byte key + 4 byte int value, repeated M times
──────────────────────────────────────────────────────
31+N+(5×M)    …      Compressed bit stream
`
`compressedDataSize` and `paddingBits` are written as `0` initially. After all chunks are encoded, `CompressionPipeline` seeks back using the offsets from `FileHeader.getCompressedDataSizeOffset()` and `FileHeader.getPaddingBitsOffset()` and patches the real values in.


```
---

## Build & Run

### Compile

```
javac -d out -sourcepath src $(find src -name "*.java")

```
### Compress

```
java -cp out CompressionApp -c -i path/to/input.txt -o path/to/output.huff

# With custom chunk size (default: 32 MB)
java -cp out CompressionApp -c -i largefile.bin --chunk 64

```
### Decompress

```
java -cp out CompressionApp -d -i path/to/output.huff -o path/to/restored.txt

# Output path is optional — defaults to stripping .huff extension
java -cp out CompressionApp -d -i output.huff

```
### CLI Flags

| Flag | Short | Description |
| --- | --- | --- |
| `--compress` | `-c` | Compress mode |
| `--decompress` | `-d` | Decompress mode |
| `--input` | `-i` | Input file path (required) |
| `--output` | `-o` | Output file path (optional) |
| `--chunk` |  | Chunk size in MB (default: 32) |


---
## Roadmap
- [ ] Tests
