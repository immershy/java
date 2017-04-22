import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zmcc on 17/4/22.
 */
public class AboutReentrantLock {

    /**
     * 可重入锁的几个关键点:
     * 1. 正如其名,锁是可重入的,意思是同一个线程可以多次获得锁
     * 2. 它是无结构的, 也就是说我们可以在一个方法里面申请锁,然后再另一个方法里面释放锁
     * 要理解第一点,看下面这个例子:
     */

    public static final ReentrantLock lock = new ReentrantLock();

    public static volatile int i = 1;

    public static void main(String[] args) {

        // 创建一个固定大小的线程池
        ExecutorService es = Executors.newFixedThreadPool(2);

        // 创建10个任务
        for (int n = 0; n < 10; n++) {
            es.submit(new Runnable() {
                public void run() {

                    lock.lock();
                    System.out.println(i++);

                }
            });
        }
        es.shutdown();

        // 运行结果分析: 代码里面lock锁没有释放,如果锁不可重入的话,最终的输出
        // 结果应该是1, 但是现在输出的是1-9,,说明两个问题:
        // 1. 同一个线程锁可以重入
        // 2. 池中线程数量为2,其中一个线程阻塞,JVM无法退出
    }
}
