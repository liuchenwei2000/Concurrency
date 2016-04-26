package chapter6.item1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 每个请求启动一个新的线程的 Web 服务器示例
 * <p>
 *     没有限制可创建线程的数量，当线程也来越多可能会耗尽服务器资源。
 * <p>
 * Created by liuchenwei on 2016/4/26
 */
public class ThreadPerTaskWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    handleRequest(connection);
                }
            }).start();
        }
    }

    private static void handleRequest(Socket connection) {
    }
}
