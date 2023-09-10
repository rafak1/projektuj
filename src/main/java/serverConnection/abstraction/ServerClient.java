package serverConnection.abstraction;

import Connection.connector.download.ServerSocketStreamReader;
import serverConnection.NoStreamReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

public interface ServerClient extends ServerClientInfo {
    InputStream getInputStream() throws IOException;
    void setSocketStreamReader(ServerSocketStreamReader socketStreamReader, ObjectOutputStream objectOutputStream) throws IOException;
    void setClientID(Long id);

    void startSocketStreamReader() throws NoStreamReaderException;
}
