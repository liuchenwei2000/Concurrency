package chapter6.item2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 1，基于线程池的 Web 服务器示例
 * <p>
 *     任务是一组逻辑工作单元，而线程则是使任务异步执行的机制。
 *     线程池简化了线程的管理工作，java.uti.concurrent 包
 *     提供了灵活的线程池实现作为 Executor 框架的一部分。
 * <p>
 *     在 Java 类库中，任务执行的主要抽象不是 Thread 而是 Executor。
 *     后者提供了一种标准的方法将任务的提交过程与执行过程解耦开来，并用 Runnale 来表示任务。
 *     Executor 的实现还提供了对生命周期的支持以及统计信息收集、应用程序管理机制和性能监视等机制。
 * <p>
 * Created by liuchenwei on 2016/4/26
 */
public class TaskExecutionWebServer {

    private static final int N_THREADS = 100;

    // 固定长度的线程池
    private static final Executor threadPool = Executors.newFixedThreadPool(N_THREADS);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();

            threadPool.execute(new Runnable() {

                @Override
                public void run() {
                    handleRequest(connection);
                }
            });
        }
    }

    private static void handleRequest(Socket connection) {
    }
}
