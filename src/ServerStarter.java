/**
 * This class contains the main required to start the server
 */
public class ServerStarter {
    public static void main(String[] args) {
        //Create a thread pool
        ThreadPool pool = new ThreadPool(10);

        //Create a server
        ChatServer server = new ChatServer(9000,pool);
        new Thread(server).start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.stop();

    }
}
