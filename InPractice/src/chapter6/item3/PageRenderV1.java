package chapter6.item3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 串行的页面渲染器
 * <p>
 *     模拟 Web 浏览器渲染网页的过程，假设网页只包含文本和图片。
 * <p>
 * Created by liuchenwei on 2016/4/27
 */
public class PageRenderV1 {

    /**
     * 对 HTML 文档进行串行处理，很容易实现，但渲染速度非常慢。
     * 因为图像下载过程的大部分时间是在等待 I/O 操作执行完成，在这期间 CPU 几乎不做任何工作。
     * 因此，这种串行执行方法没有充分地利用 CPU，使得用户在看到最终页面之前要等待过长的时间。
     */
    public void render(String html) {
        renderText(html);// 渲染文本

        List<Image> imageList = new ArrayList<>();
        List<ImageInfo> imageInfoList = scan4ImageInfo(html);
        // 下载所有图片
        for (ImageInfo imageInfo : imageInfoList) {
            imageList.add(imageInfo.downloadImage());
        }
        // 渲染所有图片
        for (Image image : imageList) {
            renderImage(image);
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
