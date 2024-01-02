package pt.iscte.condo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class RateLimitedQueue<T> {

    private final BlockingQueue<Callable<T>> queue;

    public RateLimitedQueue(@Value("${openai.rateLimit}") int maxTasksPerMinute) {
        this.queue = new LinkedBlockingQueue<>();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        long delayBetweenTasks = TimeUnit.MINUTES.toMillis(1) / maxTasksPerMinute;
        executorService.scheduleAtFixedRate(() -> {
            try {
                Callable<T> task = queue.take();
                task.call();
            } catch (InterruptedException | ExecutionException e) {
                // handle exception
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 0, delayBetweenTasks, TimeUnit.MILLISECONDS);
    }

    public void schedule(Callable<T> task) {
        queue.offer(task);
    }
}