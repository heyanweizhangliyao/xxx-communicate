package com.study.demo.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by heyanwei-thinkpad on 2019/6/15.
 *
 。
 */
public class DeadLockDemo {


    public static void main(String[] args) {


        System.out.println(System.identityHashCode(new Object()));
        System.out.println(System.identityHashCode(new Object()));
        System.out.println(System.identityHashCode(new Object()));

        new Thread(()->{
            //检测死锁，通过jdk提供的工具
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            while (true){
                long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
                if(deadlockedThreads != null && deadlockedThreads.length>0){
                    for (long id : deadlockedThreads) {

                        System.out.println(threadMXBean.getThreadInfo(id,Integer.MAX_VALUE));
                    }
                }
            }
        }).start();
        /**
         * 饥饿死锁，在单线程环境里，往executor中提交一个任务，而在这个任务中又执行另外一个任务时会发生死锁
         */
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> outtask = service.submit(() -> {
            Future<String> innerfuture = service.submit(() -> {
                System.out.println("----innertask1--------------");
                return "s1";
            });
            Future<String> innerfuture2 = service.submit(() -> {
                System.out.println("----innertask2--------------");
                return "s2";
            });
            //将发生死锁，1等2的结果，2等1的结果
            return innerfuture.get()+":"+innerfuture2.get();
        });
        try {
//        outtask.get();
        service.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /**
        第二种典型的顺序死锁,A线程获取了M锁，要获取N锁，B线程获取了N锁，要获取M锁，
         *
         */

        /**
         * 第三种动态的死顺序死锁,一个线程传入A,B，amount1,另一个线程传入B,A，amount2可能死锁
         * 伪代码
         * public void transferMoney(Account from,Account to,Amount amount){
         *     synchronized (from){
         *      synchronized (to){
         *          if(amount如何如何){
         *              from.reduce(amount);
         *              to.add(amount);
         *          }
         *      }
         *     }
         * }
         * 解决：通过System.identityHashCode,判断对象的hash值，先获取小的锁对象，然后再获取大的锁对象(亦可相反)；
         * 如果hash冲突了，可以用延时锁，即增加一个类变量，用作锁。
         */


        /**
         * 第四种，在持有锁时调用了某个外部方法，将出现活跃性问题。因为在这个外部方法中可能获取其它锁，有可能产生死锁；
         * 或者阻塞时间过长，导致其它线程无法及时获取锁。
         */

        /**
         * 第五种，资源锁
         */

        //解决：如果只有一个锁，那么不需要考虑这个问题。
        // 1在设计阶段充分考虑锁的顺序，尽量减少潜在的加锁交互数量，将获取锁的协议写入正式文档，以便排查问题。
        // 2 支持定时的锁，使用tryLock代替内置的锁。
        // 3通过线程转储信息来分析死锁

    }

}
