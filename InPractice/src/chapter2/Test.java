package chapter2;

import java.util.concurrent.*;

/**
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/24
 */
public class Test {

    public static void main(String[] args) {

        Facade.getInstance();

        for (int i = 0; i < 5; i++) {
            new Thread(new ClientTask(), "Thread" + i).start();
        }
    }

    private static class ClientTask implements Runnable {

        private static final int MAX = 100;

        @Override
        public void run() {
            for (int i = 0; i < MAX; i++) {
                String content = Thread.currentThread().getName() + "_" + i;
                Facade.getInstance().put(content);
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Facade {

        private static final Facade instance;

        static {
            instance = new Facade();
            instance.start();
        }

        private final BlockingQueue<String> contents;

        private final ExecutorService threadPool;

        private Facade(){
            this.contents = new LinkedBlockingQueue<>(500);
            // 单线程执行
            this.threadPool = Executors.newSingleThreadExecutor();
        }

        public static Facade getInstance(){
            return instance;
        }

        public void put(String content){
            try {
                contents.put(content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void start() {
            threadPool.execute(new Task());
        }

        private class Task implements Runnable {

            @Override
            public void run() {
                while(true){
                    try {
                        String content = contents.take();
                        if(content.endsWith("9")){
                            double d = 1/0;
                        }
                        System.out.println(content);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
//                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
