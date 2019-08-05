import fileTransfer.FileChunk;
import fileTransfer.FileDefinition;
import fileTransfer.FileTransferService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.logging.Level;

public class FileTransferServiceHandler implements FileTransferService.Iface {
    private HashMap<String, FileDefinition> ongoingUploads;

    private JavaServer parentServer;
    private final String serverStoragePath;

    FileTransferServiceHandler(JavaServer server, String path) {
        parentServer = server;
        ongoingUploads = new HashMap<>();

        /// create storage path
        serverStoragePath = path;
    }

    /////////////////////////////// UPLOAD APIS ////////////////////////////////////
    public void startUpload(String fileID, int fileSize) {
        if(ongoingUploads.containsKey(fileID))
            return;

        FileDefinition newfile = new FileDefinition();
        newfile.fileID = fileID;
        newfile.size = fileSize;
        newfile.data = ByteBuffer.allocate(fileSize);
        ongoingUploads.put(fileID, newfile);
    }

    public void uploadFileChunk(String fileID, FileChunk fChunk) {
        if(ongoingUploads.containsKey(fileID)) {
            FileDefinition f = ongoingUploads.get(fileID); /// upload chunk
            f.data.put(fChunk.data.array(), fChunk.offset, fChunk.size);
        }
    }

    public void endUpload(String fileID) {
        if(ongoingUploads.containsKey(fileID)) {
            try {
                ongoingUploads.get(fileID).data.flip();
                FileChannel fc = new FileOutputStream(serverStoragePath + "/" + fileID).getChannel();
                fc.write(ongoingUploads.get(fileID).data);
                fc.close();

                parentServer.logger.log(Level.FINE, "File uploaded to server");
            } catch(IOException e)
            {
                parentServer.logger.log(Level.SEVERE, "IO Exception caught");
                parentServer.logger.log(Level.SEVERE, e.toString());
            }
        }
    }

    /////////////////////////////// DOWNLOAD APIS ////////////////////////////////////
    public int startDownload(String fileID) {
        File f = new File(serverStoragePath + "/" + fileID);
        if(f.isFile())
        {
            return (int)f.length();
        } else return -1;
    }

    public FileChunk downloadFileChunk(String fileID, int offset, int size) {
        FileChunk chunk = new FileChunk();
        chunk.data = ByteBuffer.allocate(size);
        chunk.offset = offset;
        chunk.size = size;
        try {
            RandomAccessFile downFile = new RandomAccessFile(serverStoragePath + "/" + fileID, "r");
            downFile.seek(offset);
            downFile.read(chunk.data.array(), 0, size);
            downFile.close();
        } catch (IOException e) {
            parentServer.logger.log(Level.SEVERE, "IO Exception caught");
            parentServer.logger.log(Level.SEVERE, e.toString());
        }
        return chunk;
    }
}
