package chapter6.item1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 串行的 Web 服务器示例
 * <p>
 *     因为它每次只能处理一个请求，因此服务器的资源利用率非常低。
 * <p>
 * Created by liuchenwei on 2016/4/26
 */
public class SingleThreadWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    private static void handleRequest(Socket connection) {
    }
}
