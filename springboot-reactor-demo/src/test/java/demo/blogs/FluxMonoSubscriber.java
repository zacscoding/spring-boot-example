package demo.blogs;

import demo.helper.TestHelper;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

/**
 * https://javacan.tistory.com/entry/Reactor-Start-1-RS-Flux-Mono-Subscriber?category=699082
 */
public class FluxMonoSubscriber {

    @Test
    public void testFluxMono() throws Exception {
        Flux.just(1, 2, 3); // --1-2-3-|-->
        Flux.just(); // --|-->
        Flux.range(9, 4); // // --9-10-11-12-|-->
        Mono.just(1); // --1-|-->
        Mono.empty(); // --|-->
        Mono.justOrEmpty(null); // --|-->
        Mono.justOrEmpty(1); // --1-|-->

        TestHelper.doTask("Generate sequence with subscriber", () -> {
            Flux.just(1, 2, 3)
                .doOnNext(i -> TestHelper.printfln("doOnNext(%d)", i))
                .subscribe(i -> TestHelper.printfln("subscribe(%d)", i));
        });
        // ===============================================
        //> Generate flux with subscriber
        //===============================================
        //doOnNext(1)
        //subscribe(1)
        //doOnNext(2)
        //subscribe(2)
        //doOnNext(3)
        //subscribe(3)

        TestHelper.doTask("Generate sequence and subscribe after print something", () -> {
            Flux<Integer> flux = Flux.just(1, 2, 3).doOnNext(i -> TestHelper.printfln("doOnNext(%d)", i));
            TestHelper.printfln("> Generate sequence");
            flux.subscribe(i -> TestHelper.printfln("subscribe(%d)", i));
        });
        // ===============================================
        //> Generate sequence and subscribe after print something
        //===============================================
        //> Generate sequence
        //doOnNext(1)
        //subscribe(1)
        //doOnNext(2)
        //subscribe(2)
        //doOnNext(3)
        //subscribe(3)
    }

    @Test
    public void testSubscriber() throws Exception {
        TestHelper.doTask("Example of Subscriber", () -> {
            Flux<Integer> flux = Flux.just(1, 2, 3);
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            flux.subscribe(new Subscriber<Integer>() {
                private Subscription subscription;

                @Override
                public void onSubscribe(Subscription s) {
                    TestHelper.printfln("onSubscribe()");
                    this.subscription = s;
                    // Publisher 에게 데이터 요청
                    this.subscription.request(1);
                }

                @Override
                public void onNext(Integer integer) {
                    TestHelper.printfln("onNext(%d)", integer);
                    this.subscription.request(1); // pull model
                    // this.subscription.request(Long.MAX_VALUE); // push model
                }

                @Override
                public void onError(Throwable t) {
                    TestHelper.printfln("onError(%s)", t.getMessage());
                }

                @Override
                public void onComplete() {
                    TestHelper.printfln("onComplete()");
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
        });
        // ===============================================
        //> Example of Subscriber
        //===============================================
        //onSubscribe()
        //onNext(1)
        //onNext(2)
        //onNext(3)
        //onComplete()
    }

    @Test
    public void testFluxGenerate() throws Exception {
        TestHelper.doTask("Flux#generate() example", () -> {
            Consumer<SynchronousSink<Integer>> randGen = new Consumer<SynchronousSink<Integer>>() {
                private int emitCount = 0;
                private Random rand = new Random();

                @Override
                public void accept(SynchronousSink<Integer> sink) {
                    emitCount++;
                    int randData = rand.nextInt(100) + 1;
                    TestHelper.printfln("Generator sink::next(%d)", randData);
                    sink.next(randData);

                    if (emitCount == 10) {
                        TestHelper.printfln("Generator sink complete");
                        sink.complete();
                    }
                }
            };

            Flux<Integer> flux = Flux.generate(randGen);
            TestHelper.printfln("> Generate sequence");
            flux.subscribe(new BaseSubscriber<Integer>() {
                private int receiveCount = 0;

                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                    TestHelper.printfln("Subscriber#onSubscribe request first 3 items");
                    request(3L);
                }

                @Override
                protected void hookOnNext(Integer value) {
                    TestHelper.printfln("Subscriber#onNext(%d)", value);
                    receiveCount++;

                    if (receiveCount % 3 == 0) {
                        TestHelper.printfln("Subscriber request next 3 items");
                        request(3L);
                    }
                }

                @Override
                protected void hookOnComplete() {
                    TestHelper.printfln("Subscriber#onComplete()");
                }
            });
        });

        //===============================================
        //> Flux#generate() example
        //===============================================
        //> Generate sequence
        //Subscriber#onSubscribe request first 3 items
        //Generator sink::next(28)
        //Subscriber#onNext(28)
        //Generator sink::next(16)
        //Subscriber#onNext(16)
        //Generator sink::next(56)
        //Subscriber#onNext(56)
        //Subscriber request next 3 items

        //Generator sink::next(81)
        //Subscriber#onNext(81)
        //Generator sink::next(59)
        //Subscriber#onNext(59)
        //Generator sink::next(55)
        //Subscriber#onNext(55)
        //Subscriber request next 3 items

        //Generator sink::next(28)
        //Subscriber#onNext(28)
        //Generator sink::next(95)
        //Subscriber#onNext(95)
        //Generator sink::next(14)
        //Subscriber#onNext(14)
        //Subscriber request next 3 items

        //Generator sink::next(96)
        //Subscriber#onNext(96)
        //Generator sink complete
        //Subscriber#onComplete()

        TestHelper.doTask("Flux#generate(Callable<S> stateSupplier, BiFunction<S, SynchronousSink<T>, S> generator) example", () -> {
            // Callable<S> stateSupplier, BiFunction<S, SynchronousSink<T>, S> generator
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            Flux<String> flux = Flux.generate(new Callable<Integer>() {
                private final Integer val = 1;

                @Override
                public Integer call() throws Exception {
                    TestHelper.printfln("StateSupplier#call():%d", val);
                    return val;
                }
            }, new BiFunction<Integer, SynchronousSink<String>, Integer>() {
                private final String format = "3 x %d = %d";

                @Override
                public Integer apply(Integer state, SynchronousSink<String> sink) {
                    sink.next(String.format(format, state, state));

                    if (state == 10) {
                        sink.complete();
                        countDownLatch.countDown();
                    }

                    return state + 1;
                }
            });

            flux.subscribe(s -> TestHelper.printfln("Subscriber %s", s));
            countDownLatch.await();
        });

        //===============================================
        //> Flux#generate(Callable<S> stateSupplier, BiFunction<S, SynchronousSink<T>, S> generator) example
        //===============================================
        //StateSupplier#call():1
        //Subscriber 3 x 1 = 1
        //Subscriber 3 x 2 = 2
        //Subscriber 3 x 3 = 3
        //Subscriber 3 x 4 = 4
        //Subscriber 3 x 5 = 5
        //Subscriber 3 x 6 = 6
        //Subscriber 3 x 7 = 7
        //Subscriber 3 x 8 = 8
        //Subscriber 3 x 9 = 9
        //Subscriber 3 x 10 = 10
    }
}
