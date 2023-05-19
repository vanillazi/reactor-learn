package cn.vanillazi.learn;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var s=Mono.defer(()->{
            System.out.println("do action 1");
            return Mono.just(1);
        });
        s=s.then(Mono.defer(()->{
            System.out.println("do action then");
            return Mono.empty();
        }));
        s.log().subscribeOn(Schedulers.parallel()).subscribe((o)->{
            System.out.println("subscribe");
        });
        TimeUnit.SECONDS.sleep(1);
    }
}