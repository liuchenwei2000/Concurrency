package chapter7.item1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 5，处理不可中断的阻塞
 * <p>
 *     并非所有的可阻塞方法或阻塞机制都能响应中断：
 *     如果一个线程由于执行同步的 Socket I/O 或者等待获得内置锁而阻塞，
 *     那么中断请求只能设置线程的中断状态，除此之外没有其他任何作用。
 *     对于那些由于执行不可中断操作而被阻塞的线程，可以使用类似于中断的手段
 *     来停止这些线程，但这要求必须知道线程阻塞的原因。
 * <p>
 *     在服务器应用程序中，最常见的阻塞 I/O 形式就是对套接字的读取和写入。
 *     虽然 InputStream 和 OutputStream 中的 read 和 write 等方法都不会影响中断，
 *     但通过关闭底层的套接字，可以使得由于执行 read 或 write 等方法而被阻塞的线程抛出 SocketException。
 * <p>
 * Created by liuchenwei on 2016/4/29.
 */
public class ReaderThread extends Thread {

    private final Socket socket;
    private final InputStream inputStream;

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
    }

    /**
     * 为了结束某个用户的连接或者关闭服务器，重写了 interrupt 方法，
     * 使其既能处理标准的中断，也能关闭底层的套接字。
     * 因此，无论 ReaderThread 线程是在 read 方法中阻塞还是在
     * 某个可中断的阻塞方法中阻塞 ，都可以被中断并停止执行当前的工作。
     */
    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            super.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int count = inputStream.read(buffer);
                if (count < 0) {
                    break;
                } else {
                    processBuffer(buffer, count);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Thread is shutting down");
            // 允许线程退出
        }
    }

    private void processBuffer(byte[] buffer, int count) {
    }
}
