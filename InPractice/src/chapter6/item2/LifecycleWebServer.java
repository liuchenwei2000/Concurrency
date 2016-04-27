package chapter6.item2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 4，支持关闭操作的 Web 服务器示例
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/26
 */
public class LifecycleWebServer {

    private static final int N_THREADS = 100;

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(N_THREADS);

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!threadPool.isShutdown()) {
            final Socket connection = socket.accept();

            threadPool.execute(new Runnable() {

                @Override
                public void run() {
                    handleRequest(connection);
                }
            });
        }
    }

    public void stop() throws IOException {
        threadPool.shutdown();
    }

    private static void handleRequest(Socket connection) {
    }
}
