package bumh3r.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolThreads {

    private ExecutorService executorService;
    private static PoolThreads instance;

    public static PoolThreads getInstance() {
        if (instance == null) {
            instance = new PoolThreads();
        }
        return instance;
    }

    public PoolThreads() {
//        executorService = Executors.newCachedThreadPool();
        executorService = Executors.newFixedThreadPool(3);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void close() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
            instance = null;
        }
        System.out.println("PoolThreads closed: " + executorService.isShutdown() + " - " + executorService.isTerminated());

    }

}
