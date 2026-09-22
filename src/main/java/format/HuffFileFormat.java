package format;

import exception.InvalidFormatException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class HuffFileFormat {

    public void writeHeader(FileHeader header, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.write(header.getMagicNum());
        dos.writeShort(header.getVersion());
        dos.writeLong(header.getOriginalFileSize());

        byte[] nameBytes = header.getOriginalFileName().getBytes(StandardCharsets.UTF_8);
        dos.writeInt(nameBytes.length);
        dos.write(nameBytes);
        dos.writeLong(header.getCompressedDataSize());
        dos.writeByte(header.getPaddingBits());
        dos.writeInt(header.getFrequencyTable().size());

        for (Map.Entry<Byte,Integer> e : header.getFrequencyTable().entrySet()) {
            dos.writeByte(e.getKey());
            dos.writeInt(e.getValue());
        }

        dos.flush();
    }

    public FileHeader readHeader(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        FileHeader header = new FileHeader();
        try {
            byte[] magic = dis.readNBytes(4);
            if (!Arrays.equals(magic, FileHeader.MAGIC_NUMBER)) {
                throw new InvalidFormatException(InvalidFormatException.Reason.MAGIC_NUMBER, "HUFF is missing it got: " + Arrays.toString(magic));
            }
            header.setMagicNum(magic);
            short ver = dis.readShort();
            if (ver != FileHeader.CURRENT_VERSION) {
                throw new InvalidFormatException(InvalidFormatException.Reason.UNSUPPORTED_VERSION, "Version " + ver + " unsupported");
            }
            header.setVersion(ver);
            header.setOriginalFileSize(dis.readLong());
            int nameLen = dis.readInt();
            header.setOriginalFileName(new String(dis.readNBytes(nameLen), StandardCharsets.UTF_8));
            header.setCompressedDataSize(dis.readLong());
            header.setPaddingBits(dis.readUnsignedByte());
            int mapSize = dis.readInt();

            Map<Byte,Integer> freqMap = new HashMap<>();
            for (int i = 0; i < mapSize; i++) {
                freqMap.put(dis.readByte(), dis.readInt());
            }
            header.setFrequencyTable(freqMap);
        }catch(EOFException e){
            throw new InvalidFormatException(InvalidFormatException.Reason.CORRUPTED_HEADER,"Header is missin");
        }

        return header;
    }
}
