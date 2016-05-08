package chapter16.item2;

/**
 * 模拟单例类
 * <p>
 * <p>
 * Created by liuchenwei on 2016/5/8
 */
class Singleton {

    private int intValue;
    private String stringValue;
    private Object refValue;
    // 其他更多的域

    public Singleton() {
        this.intValue = 10;
        this.stringValue = "Hello";
        this.refValue = new Object();
    }
}
