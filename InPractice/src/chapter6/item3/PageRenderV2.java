package chapter6.item3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 使用 Future 实现页面渲染器
 * <p>
 *     渲染文本任务和下载图像任务并发地执行，将会更快的渲染网页。
 * <p>
 * Created by liuchenwei on 2016/4/27
 */
public class PageRenderV2 {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 首先将渲染过程分解为两个任务：一个是渲染所有的文本，另一个是下载所有的对象。
     * 因为前者是 CPU 密集型，后者是 I/O 密集型，因此该方法即使在单 CPU 系统上也能提升性能。
     * Callable 和 Future 有助于表示这些协同任务之间的交互。
     */
    public void render(String html) {
        final List<ImageInfo> imageInfoList = scan4ImageInfo(html);

        Callable<List<Image>> imageTask = new Callable<List<Image>>() {

            @Override
            public List<Image> call() throws Exception {
                List<Image> imageList = new ArrayList<>();

                // 下载所有图片
                for (ImageInfo imageInfo : imageInfoList) {
                    imageList.add(imageInfo.downloadImage());
                }
                return imageList;
            }
        };

        Future<List<Image>> future = executorService.submit(imageTask);
        renderText(html);// 渲染文本

        try {
            List<Image> images = future.get();
            // 渲染所有图片
            for (Image image : images) {
                renderImage(image);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();// 重设线程的中断状态
            future.cancel(true);// 由于不需要结果，因此取消任务
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
