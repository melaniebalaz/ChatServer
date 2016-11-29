/**
 * This class contains the main required to start the server
 */
public class ServerStarter {
    public static void main(String[] args) {
        //Create a thread pool
        ThreadPool pool = new ThreadPool(10);

        //Create a server
        ChatServer server = new ChatServer(9000,pool);
        server.run();
        server.stop();

    }
}
