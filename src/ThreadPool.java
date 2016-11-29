import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    protected ExecutorService threadPool;

    /**
     * Instantiate the thread pool with the passed amount of threads
     * @param amountOfThreads
     */
    public ThreadPool(int amountOfThreads){
        threadPool = Executors.newFixedThreadPool(amountOfThreads);
    }

    /**
     * The thread pool executes the runnable task it was passed
     * @param newTask
     */
    public void addTask(Runnable newTask){
        threadPool.execute(newTask);
    }

    /**
     * Shuts down the thread pool
     */
    public void shutDownPool(){
        threadPool.shutdown();
    }
}
