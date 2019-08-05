namespace java fileTransfer

struct FileDefinition {
    1: binary data,
    2: string fileID,
    3: i32 size,
}

struct FileChunk {
    1: binary data,
    2: i32 size,
    3: i32 offset,
}

service FileTransferService {
    void startUpload(1:string fileID, 2:i32 fileSize),
    void uploadFileChunk(1:string fileID, 2:FileChunk data),
    void endUpload(1:string fileID),

    i32 startDownload(1:string fileID),
    FileChunk downloadFileChunk(1:string fileID, 2:i32 offset, 3:i32 size),
}