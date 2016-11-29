import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


/**
 * The server runs and is able to accept client requests
 */
public class ChatServer implements Runnable {
    private int port;
    private boolean running = true;
    private ThreadPool pool;
    private ServerSocket serverSocket; //The Main Server Socket
    private Vector<Socket> clientSockets = new Vector<>(); //A vector of sockets

    /**
     * Constructor expects a port and a Thread Pool
     * @param pPort
     * @param pPool
     */
    public ChatServer(int pPort,ThreadPool pPool){
        port = pPort;
        pool = pPool;
    }

    /**
     * Instantiates the main server socket to listen to requests on the specified port
     */
    private void openServerSocket(){
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException exception){
            throw new RuntimeException("Cannot create Socket");
        }

    }

    /**
     * This is what the server should do when it is running
     */
    public void run(){

        openServerSocket();

        System.out.println("Started ChatServer...");
        while(running){
            //Create a new socket each time
            Socket clientSocket;
            try {

                //Accept incoming connections and save them into client Sockets, accept is a blocking call
                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection...");

                //Add each new socket to the vector of sockets
                clientSockets.add(clientSocket);

            }
            catch (IOException exception){
                if (!running){
                    System.out.println("Server shutdown");
                    return;
                }
                //RuntimeException(Throwable, cause)
                throw new RuntimeException("Cannot accept request", exception);
            }
            //Create a new runnable Task with the client Socket, and the list of all so far existing client Sockets
            SocketHandlerTask runnableTask = new SocketHandlerTask(clientSocket,this);

            //Hand a runnable task to the thread Pool
            pool.addTask(runnableTask);
        }
    }

    /**
     * This is what the server should do when it is stopped
     */
    public void stop(){

        running = false;

        //close all the client socket connections
        try {
            for (Socket socket : clientSockets){
                socket.close();
            }
        }
        catch (IOException exception){
            throw new RuntimeException("Error closing sockets");
        }

        //close the Server Socket
        try {
            serverSocket.close();
        }
        catch (IOException exception){
            throw new RuntimeException("Error closing down the server");
        }

        //if the server is stopped, shut down the threadpool
        pool.shutDownPool();

    }

    public Vector<Socket> getClientSockets(){
        return clientSockets;
    }


}
