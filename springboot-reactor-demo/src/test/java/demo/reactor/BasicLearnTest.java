package demo.reactor;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class BasicLearnTest {

    @Test
    public void flux() throws Exception {
        Flux<String> just = Flux.just("1", "2", "3");
    }

    @Test
    public void flux_collectingElts() throws Exception {
        List<Integer> elts = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
            .log()
            .subscribe(elts::add);

        assertThat(elts).containsExactly(1, 2, 3, 4);
//        17:07:56.296 [main] INFO reactor.Flux.Array.1 - | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
//        17:07:56.299 [main] INFO reactor.Flux.Array.1 - | request(unbounded)
//        17:07:56.299 [main] INFO reactor.Flux.Array.1 - | onNext(1)
//        17:07:56.299 [main] INFO reactor.Flux.Array.1 - | onNext(2)
//        17:07:56.299 [main] INFO reactor.Flux.Array.1 - | onNext(3)
//        17:07:56.299 [main] INFO reactor.Flux.Array.1 - | onNext(4)
//        17:07:56.300 [main] INFO reactor.Flux.Array.1 - | onComplete()
    }

    /**
     * http://tech.kakao.com/2018/05/29/reactor-programming/
     */
    @Test
    public void temp() throws Exception {
        final List<String> basket1 = Arrays.asList("kiwi", "orange", "lemon", "orange", "lemon", "kiwi");
        final List<String> basket2 = Arrays.asList("banana", "lemon", "lemon", "kiwi");
        final List<String> basket3 = Arrays.asList("strawberry", "orange", "lemon", "grape", "strawberry");
        final List<List<String>> baskets = Arrays.asList(basket1, basket2, basket3);
        final Flux<List<String>> basketFlux = Flux.fromIterable(baskets);

        Mono<List<String>> distinctFruits = Flux.fromIterable(basket1).distinct().collectList();
        Flux<GroupedFlux<String, String>> groupedFluxFlux = Flux.fromIterable(basket1).groupBy(fruit -> fruit);
        groupedFluxFlux.subscribe(param -> {
            System.out.println(param.key());
        });
        groupedFluxFlux.concatMap(groupedFlux -> groupedFlux.count()
            .map(count -> {
                System.out.println("Key : " + groupedFlux.key() + ", count : " + count);
                final Map<String, Long> fruitCount = new LinkedHashMap<>();
                fruitCount.put(groupedFlux.key(), count);
                return fruitCount;
            })
        ).reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() {{
            putAll(accumulatedMap);
            putAll(currentMap);
        }});
        /*basketFlux.concatMap(basket -> {
            // final Mono<List<String>> distinctFruits = Flux.fromIterable(basket).distinct().collectList();
            System.out.println(basket.getClass().getName());
            return null;
        });*/
    }
}
