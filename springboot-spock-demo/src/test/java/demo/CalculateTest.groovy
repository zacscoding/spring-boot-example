package demo

import spock.lang.Specification

/**
 * http://woowabros.github.io/study/2018/03/01/spock-test.html
 *
 * @author zacconding* @Date 2018-11-27
 * @GitHub : https://github.com/zacscoding
 */
class CalculateTest extends Specification {

    def setup() {
        println("setup...")
    }

    def "test_add"() {
        expect:
        Calculator.add(num1, num2) == result

        where:
        num1 | num2 | result
        10   | 5    | 15
        1    | 2    | 3
    }

    def "test_add2"() {
        given:
        int number1 = 10
        int number2 = 5

        when:
        int result = Calculator.add(number1, number2)

        then:
        result == 11
    }

    def "test_throwException"() {
        when:
        Integer.parseInt("a")

        then:
        def error = thrown(NumberFormatException.class)
    }
}
