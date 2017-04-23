import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 实现同时最多有两个线程持有锁
 */
public class TwinsLock implements Lock {

    private Sync sync = new Sync();

    private Sync sync1 = new Sync();

    public void lock() {
        sync.acquire(1);
        sync1.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {
        sync.release(1);
        sync1.release(1);
    }

    public Condition newCondition() {
        return null;
    }

    public static volatile int a = 1;

    public static void main(String[] args) {


        final TwinsLock twinsLock = new TwinsLock();

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                public void run() {
                    twinsLock.lock();
                    try {
                        System.out.println((a++) + "                  ");
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        twinsLock.unlock();
                    }
                }
            }).start();
        }


    }

    // 独占锁 共享锁 公平锁 非公平锁

    static class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                return true;
            }
            if (compareAndSetState(1, 2)) {
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (compareAndSetState(2, 1)) {
                return true;
            }
            if (compareAndSetState(1, 0)) {
                return true;
            }
            return false;
        }
    }

}
