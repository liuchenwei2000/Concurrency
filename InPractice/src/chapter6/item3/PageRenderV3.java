package chapter6.item3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 使用 CompletionService 实现页面渲染器
 * <p>
 *     通过使用 CompletionService 可以从两方面来提高页面渲染的性能：
 *     缩短总运行时间以及提高响应性。
 *     为每一个图片的下载都创建一个独立任务，并在线程池中执行它们，
 *     从而将串行的下载过程转换为并行，这将减少下载所有图片的总时间。
 *     通过从 CompletionService 中获取结果以及使每个图片在下载完成后立刻显出来，
 *     能使用户获得一个更加动态和更高响应性的用户界面。
 * <p>
 *     如果向 Executor 提交了一组计算任务，并且希望在计算完成后获得结果，
 *     那么可以保留与每个任务关联的 Future，然后反复使用 get 方法，
 *     同时将参数 tomeout 设为 0，从而通过轮询来判断任务是否完成。
 *     这种方法虽然可行，但很繁琐，可以使用 CompletionService 来完成上述功能。
 * <p>
 *     CompletionService 将 Executor 和 BlockingQueue 的功能融合在一起。
 *     可以将 Callable 任务提交给它来执行，然后使用类似队列操作的 take 和 poll
 *     等方法来获得已完成的结果，而这些结果会在完成时被封装为 Future。
 * <p>
 * Created by liuchenwei on 2016/4/27
 */
public class PageRenderV3 {

    private final Executor executor = Executors.newFixedThreadPool(5);

    public void render(String html) {
        final List<ImageInfo> imageInfoList = scan4ImageInfo(html);

        // ExecutorCompletionService 实现了 CompletionService，将计算部分委托给 Executor。
        CompletionService<Image> completionService = new ExecutorCompletionService<>(executor);

        for (final ImageInfo imageInfo : imageInfoList) {
            completionService.submit(new Callable<Image>() {

                @Override
                public Image call() throws Exception {
                    return imageInfo.downloadImage();
                }
            });
        }

        renderText(html);// 渲染文本

        try {
            for (int i = 0; i < imageInfoList.size(); i++) {
                Future<Image> future = completionService.take();
                Image image = future.get();
                renderImage(image);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();// 重设线程的中断状态
        } catch (ExecutionException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
        }
    }

    private void renderText(String html) {
    }

    private List<ImageInfo> scan4ImageInfo(String html) {
        return new ArrayList<>();
    }

    private void renderImage(Image image) {
    }
}
