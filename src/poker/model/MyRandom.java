package poker.model;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Becker
 */
public class MyRandom extends Random {

    private static MyRandom INSTANCE = null;

    public static MyRandom getRandom() {
        if (INSTANCE == null) INSTANCE = new MyRandom();
        return INSTANCE;
    }

    public static double uniform() {
        return getRandom().nextDouble();
    }

    public static double gaussian(double stdDev) {
        return gaussian(0.0, stdDev);
    }

    public static double gaussian(double mean, double stdDev) {
        return mean + stdDev * getRandom().nextGaussian();
    }

    public static double exponential(double lambda) {
        return -Math.log(1 - getRandom().nextDouble()) / lambda;
    }

    private static final long serialVersionUID = 704114066645185259L;

    private final ReentrantLock lock = new ReentrantLock();

    private long u;
    private long v = 4101842887655102017L;
    private long w = 1;

    private MyRandom() {
        lock.lock();
        u = System.nanoTime() ^ v;
        nextLong();
        v = u;
        nextLong();
        w = v;
        nextLong();
        lock.unlock();
    }

    @Override
    public long nextLong() {
        lock.lock();
        try {
            u = u * 2862933555777941757L + 7046029254386353087L;
            v ^= v >>> 17;
            v ^= v << 31;
            v ^= v >>> 8;
            w = 4294957665L * (w & 0xffffffff) + (w >>> 32);
            long x = u ^ (u << 21);
            x ^= x >>> 35;
            x ^= x << 4;
            long ret = (x + v) ^ w;
            return ret;
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected int next(int bits) {
        return (int) (nextLong() >>> (64 - bits));
    }

}
