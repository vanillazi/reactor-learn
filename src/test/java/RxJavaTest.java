import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.internal.schedulers.NewThreadWorker;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class RxJavaTest {

    public static final boolean throwTest=true;

    public static void main(String[] args) throws InterruptedException {
        var f=Flowable.fromSupplier(()->{
            System.out.println(Thread.currentThread().getName()+":doAction");
            if(throwTest) {
                throw new RuntimeException("exception in doAction");
            }else {
                return "test";
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(new Scheduler() {
                    @Override
                    public @NonNull Worker createWorker() {
                        return new NewThreadWorker(new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return new Thread(r);
                            }
                        });
                    }
                });
        f.subscribe((s)->{
            System.out.println(Thread.currentThread().getName()+":onNext");
        },e->{
            System.out.println(Thread.currentThread().getName()+":onError");
        },()->{
            System.out.println(Thread.currentThread().getName()+":onComplete");
        });
        TimeUnit.SECONDS.sleep(3);

    }



}
