package com.chong.mcspczuul;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class McThreadLocalTest {
    static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
    static ExecutorService pool = Executors.newFixedThreadPool(2);
    public static void main(String[] args) {
        for(int i=0;i<100;i++) {
            int j = i;
            pool.execute(new Thread(new Runnable() {
                @Override
                public void run() {

//                    System.out.println("main:" + Thread.currentThread().getName());
                    McThreadLocalTest.threadLocal.set("猿天地"+j);
                    new Service().call();
                }
        }));
        }
    }
}
class Service {
    public void call() {
        McThreadLocalTest.pool.execute(new Runnable() {
            @Override
            public void run() {
//                System.out.println("service:" + Thread.currentThread().getName());
                new Dao().call();
            }
        });
    }
}
class Dao {
    public void call() {
//        System.out.println("dao:" + Thread.currentThread().getName());
        System.out.println("Dao:" + McThreadLocalTest.threadLocal.get());
    }
}
