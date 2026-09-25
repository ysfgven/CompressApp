package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FrequencyAnalyzerTest {
    private FrequencyAnalyzer frequencyAnalyzer;

    @BeforeEach
    void setUp() {
        frequencyAnalyzer = new FrequencyAnalyzer();
    }

    @Test
    void analyze_emptyStream_returnsEmptyMap() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Map<Byte, Integer> result = frequencyAnalyzer.analyze(inputStream);
        assertTrue(result.isEmpty());
    }

    @Test
    void analyze_knownText_countsFrequenciesCorrectly() throws IOException {
        byte[] inputData = "AAABBC".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Map<Byte, Integer> result = frequencyAnalyzer.analyze(inputStream);
        assertEquals(3,result.size());
        assertEquals(3,result.get((byte)'A'));
        assertEquals(2,result.get((byte)'B'));
        assertEquals(1,result.get((byte)'C'));
    }
    @Test
    void analyze_binaryDatahandlesNegativeAndExtremeBytes() throws IOException {
        byte[] inputData = new byte[]{0, (byte)255, (byte)255, -128};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Map<Byte, Integer> result = frequencyAnalyzer.analyze(inputStream);
        assertEquals(3,result.size());
        assertEquals(1,result.get((byte)0));
        assertEquals(2,result.get((byte)255));
        assertEquals(1, result.get((byte)(-128)));
    }

}