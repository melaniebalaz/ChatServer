import java.io.IOException;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Vector;

/**
 * This is the Class for the task which one thread will have to handle.
 * It needs to get the input from the socket and then send this input to all other sockets
 */
public class SocketHandlerTask implements Runnable{
    Socket clientSocket;
    ChatServer server;

    SocketHandlerTask(Socket pClientSocket, ChatServer pServer){
        clientSocket = pClientSocket;
        server = pServer;
    }

    @Override
    public void run() {

        while(true){
            String chatMessage;

            try {
                //GET INPUT from the socket and store it in a reader
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                chatMessage = reader.readLine();
            } catch (IOException exception) {
                throw new RuntimeException("Could not get input", exception);
            }

            //WRITE INPUT to all other sockets

            //Get all other current sockets locally
            Vector<Socket> clientSockets = server.getClientSockets();

            //TODO SYNCHRONIZED STATEMENT
            //WRITE TO SOCKETS
            synchronized (this) {
                try {
                    //Write to each socket
                    for (Socket socket : clientSockets) {
                        if (socket != clientSocket){
                            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                            writer.println(chatMessage);
                        }
                    }
                } catch (IOException exception) {
                    throw new RuntimeException("Could not sent to other sockets");
                }
            }
        }
    }

}
