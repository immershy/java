import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by zmcc on 17/4/23.
 */
public class TwinsLockV2 implements Lock {


    public static volatile int a = 1;

    public static void main(String[] args) {

        final TwinsLockV2 lock = new TwinsLockV2();

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                public void run() {
                    lock.lock();
                    try {
                        System.out.println(a++);
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }).start();
        }

    }

    public void lock() {
        sync.acquireShared(1);
    }

    public void unlock() {
        sync.releaseShared(1);
    }

    private Sync sync = new Sync();

    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected int tryAcquireShared(int arg) {
            // 如果返回结果大于等于0,表示获取锁成功;否则失败
            if (compareAndSetState(0, 1)) {
                return 0;
            }
            if (compareAndSetState(1, 2)) {
                return 0;
            }
            return -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            // 释放成功或者失败
            if (compareAndSetState(2, 1)) {
                return true;
            }
            if (compareAndSetState(1, 0)) {
                return true;
            }
            return false;
        }
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    public Condition newCondition() {
        return null;
    }
}
