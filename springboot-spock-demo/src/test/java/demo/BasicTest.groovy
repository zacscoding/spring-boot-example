package demo

import demo.events.Publisher
import demo.events.Subscriber
import spock.lang.Specification

/**
 * http://spockframework.org/spock/docs/1.0/spock_primer.html
 *
 * @author zacconding* @Date 2018-11-27
 * @GitHub : https://github.com/zacscoding
 */
class BasicTest extends Specification {


    def "test when and then"() {
        Stack stack = new Stack()
        when:
        stack.push("hivava")

        then:
        !stack.isEmpty()
        stack.size() == 1
        stack.pop().equals("hivava")
    }

    def "exception condition"() {
        Stack stack = new Stack()

        when:
        stack.pop()

        then:
        //thrown(EmptyStackException)
        EmptyStackException e = thrown()
        stack.empty()
    }

    def "events are published to all subscribers"() {
        def subscriber1 = Mock(Subscriber)
        def subscriber2 = Mock(Subscriber)

        def publisher = new Publisher()
        publisher.register(subscriber1)
        publisher.register(subscriber2)

        when:
        publisher.fire("event")

        then:
        1 * subscriber1.receive("event")
        1 * subscriber2.receive("event")
    }

    def "data table"() {
        expect:
        println(String.format("a : %d | b : %d | c : %d", a, b, c))
        Math.max(a, b) == c

        where:
        a | b | c
        1 | 2 | 2
        2 | 3 | 3
        1 | 1 | 1

        /*
        output
        a : 1 | b : 2 | c : 2
        a : 2 | b : 3 | c : 3
        a : 1 | b : 1 | c : 1
         */
    }

    def "data pipes"() {
        expect:

        println(String.format("a : %d | b : %d | c : %d", a, b, c))
        Math.max(a, b) == c

        where:
        a << [3, 7, 0]
        b << [5, 0, 0]
        c << [5, 7, 0]
        /*
        output
        a : 3 | b : 5 | c : 5
        a : 7 | b : 0 | c : 7
        a : 0 | b : 0 | c : 0
         */
    }
}
