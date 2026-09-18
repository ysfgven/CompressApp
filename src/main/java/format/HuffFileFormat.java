package format;

import exception.InvalidFormatException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class HuffFileFormat {

    public void writeHeader(FileHeader header, OutputStream out){

        DataOutputStream dos = new DataOutputStream(out);
        try{
            dos.write(header.getMagicNum());
            dos.writeShort(header.getVersion());

            dos.writeLong(header.getOriginalFileSize());

            byte[] fileNameBytes = header.getOriginalFileName().getBytes(StandardCharsets.UTF_8);//to ensure no language problem
            dos.writeInt(fileNameBytes.length);
            dos.write(fileNameBytes);

            dos.writeLong(header.getCompressedDataSize());

            dos.writeByte(header.getPaddingBits());

            dos.writeInt(header.getFrequencyTable().size());
            for (Map.Entry<Byte, Integer> entry : header.getFrequencyTable().entrySet()) {
                dos.writeByte(entry.getKey());
                dos.writeInt(entry.getValue());
            }

            dos.flush();
        }catch (IOException e) {
            System.out.println("error");//todo:will code later
        }


    }

    public FileHeader readHeader(InputStream in) throws IOException{
        DataInputStream dis = new DataInputStream(in);
        FileHeader fileHeader = new FileHeader();

        try {

            //checking signatures
            byte[] magic = dis.readNBytes(4);
            if(!Arrays.equals(magic,FileHeader.MAGIC_NUMBER)){
                throw new InvalidFormatException(InvalidFormatException.Reason.MAGIC_NUMBER,"Expected HUFF, got: \"" + Arrays.toString(magic));
            }
            fileHeader.setMagicNum(magic);

            short version = dis.readShort();
            if(version != FileHeader.CURRENT_VERSION) {
                throw new InvalidFormatException(InvalidFormatException.Reason.UNSUPPORTED_VERSION, "Version " + version + " not supported");
            }
            fileHeader.setVersion(version);
            //adding original file size
            fileHeader.setOriginalFileSize(dis.readLong());

            //for name
            int size = dis.readInt();
            byte[] fileNameBytes = dis.readNBytes(size);
            String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);
            fileHeader.setOriginalFileName(fileName);
            //adding compressed data size
            fileHeader.setCompressedDataSize(dis.readLong());
            //adding padding bits to object
            fileHeader.setPaddingBits(dis.readByte());

            //to read map
            int mapSize = dis.readInt();
            Map<Byte,Integer> freqMap = new HashMap<>();
            for (int i = 0; i < mapSize; i++) {
                byte key = dis.readByte();
                int value = dis.readInt();
                freqMap.put(key,value);
            }
            fileHeader.setFrequencyTable(freqMap);
        } catch (EOFException e) {
            throw new InvalidFormatException(InvalidFormatException.Reason.CORRUPTED_HEADER, "Header unexpectedly incomplete");
        } catch (IOException e) {
            throw new InvalidFormatException(InvalidFormatException.Reason.CORRUPTED_HEADER, "Failed to read header: " + e.getMessage());
        }

        return fileHeader;
    }
}
