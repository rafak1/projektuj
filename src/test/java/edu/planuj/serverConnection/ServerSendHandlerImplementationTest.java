package edu.planuj.serverConnection;

import edu.planuj.Connection.protocol.Packable;
import edu.planuj.Connection.protocol.RespondInformation;
import edu.planuj.clientConnection.abstraction.NotificationObserver;
import edu.planuj.serverConnection.ServerSendHandlerImplementation;
import edu.planuj.serverConnection.abstraction.NotificationPackageVisitor;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import edu.planuj.serverConnection.abstraction.ServerClient;
import edu.planuj.serverConnection.abstraction.SocketSelector;

import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerSendHandlerImplementationTest {

    Packable packable1;
    Packable packable2;
    ServerClient serverClient1;
    ServerClient serverClient2;
    ObjectOutput objectOutputStream1;
    ObjectOutput objectOutputStream2;
    RespondInformation respondInformation;
    SocketSelector socketSelector;

    @Test
    void send() {
        //given
        NotificationPackageVisitor notificationPackageVisitor = Mockito.mock(NotificationPackageVisitor.class);
        ServerSendHandlerImplementation serverSendHandlerImplementation = new ServerSendHandlerImplementation(notificationPackageVisitor);
        ServerClient serverClient = Mockito.mock(ServerClient.class);
        ObjectOutput objectOutputStream = Mockito.mock(ObjectOutput.class);
        Packable packable = Mockito.mock(Packable.class);
        Mockito.when(serverClient.getObjectOutput()).thenReturn(objectOutputStream);
        //when
        assertDoesNotThrow(()-> serverSendHandlerImplementation.send(packable, serverClient));
        //then
        assertDoesNotThrow(()-> Mockito.verify(objectOutputStream, Mockito.times(1)).writeObject(packable));
    }

    @Test
    void nullOutputStreamSend(){
        //given
        NotificationPackageVisitor notificationPackageVisitor = Mockito.mock(NotificationPackageVisitor.class);
        ServerSendHandlerImplementation serverSendHandlerImplementation = new ServerSendHandlerImplementation(notificationPackageVisitor);
        ServerClient serverClient = Mockito.mock(ServerClient.class);
        ObjectOutput objectOutputStream = Mockito.mock(ObjectOutput.class);
        Packable packable = Mockito.mock(Packable.class);
        Mockito.when(serverClient.getObjectOutput()).thenReturn(null);
        Exception e = null;
        //when
        try {
            serverSendHandlerImplementation.send(packable, serverClient);
        } catch (Exception ex) {
            e = ex;
        }
        //then
        assertNotNull(e);
        assertDoesNotThrow(()-> Mockito.verify(objectOutputStream, Mockito.times(0)).writeObject(packable));
    }

    void prepareForSendResponses(){
        respondInformation = Mockito.mock(RespondInformation.class);
        socketSelector = Mockito.mock(SocketSelector.class);
        packable1 = Mockito.mock(Packable.class);
        packable2 = Mockito.mock(Packable.class);
        Mockito.when(respondInformation.getResponses()).thenReturn(Map.of(1L, List.of(packable1), 2L , List.of(packable2)));
        serverClient1 = Mockito.mock(ServerClient.class);
        serverClient2 = Mockito.mock(ServerClient.class);
        Mockito.when(socketSelector.getExistingClientsFromId(Mockito.any())).thenReturn(Stream.of(new Pair<>(1L, serverClient1), new Pair<>(2L, serverClient2)));
        objectOutputStream1 = Mockito.mock(ObjectOutput.class);
        objectOutputStream2 = Mockito.mock(ObjectOutput.class);
        Mockito.when(serverClient1.getObjectOutput()).thenReturn(objectOutputStream1);
        Mockito.when(serverClient2.getObjectOutput()).thenReturn(objectOutputStream2);
    }

    @Test
    void sendResponses() {
        //given
        NotificationPackageVisitor notificationPackageVisitor = Mockito.mock(NotificationPackageVisitor.class);
        ServerSendHandlerImplementation serverSendHandlerImplementation = new ServerSendHandlerImplementation(notificationPackageVisitor);
        prepareForSendResponses();

        //when
        serverSendHandlerImplementation.sendResponses(respondInformation, socketSelector);

        //then
        verify(serverClient1, Mockito.times(1)).getObjectOutput();
        verify(serverClient2, Mockito.times(1)).getObjectOutput();
        assertDoesNotThrow(()->verify(objectOutputStream1, Mockito.times(1)).writeObject(packable1));
        assertDoesNotThrow(()->verify(objectOutputStream2, Mockito.times(1)).writeObject(packable2));
    }

    @Test
    void OneThrowsSendResponses(){
        //given
        NotificationPackageVisitor notificationPackageVisitor = Mockito.mock(NotificationPackageVisitor.class);
        ServerSendHandlerImplementation serverSendHandlerImplementation = new ServerSendHandlerImplementation(notificationPackageVisitor);
        prepareForSendResponses();
        Mockito.when(serverClient1.getObjectOutput()).thenReturn(null);

        //when
        serverSendHandlerImplementation.sendResponses(respondInformation, socketSelector);

        //then
        verify(serverClient1, Mockito.times(1)).getObjectOutput();
        verify(serverClient2, Mockito.times(1)).getObjectOutput();
        assertDoesNotThrow(()->verify(objectOutputStream1, Mockito.times(0)).writeObject(packable1));
        assertDoesNotThrow(()->verify(objectOutputStream2, Mockito.times(1)).writeObject(packable2));
    }

}