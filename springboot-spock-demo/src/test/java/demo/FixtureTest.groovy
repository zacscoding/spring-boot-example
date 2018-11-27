package demo

import javafx.beans.binding.When
import spock.lang.Specification

/**
 *
 * @author zacconding* @Date 2018-11-27
 * @GitHub : https://github.com/zacscoding
 */
class FixtureTest extends Specification {

    def setup() {
        println("setup() is called")
    }

    def cleanup() {
        println("cleanup() is called")
    }

    def setupSpec() {
        println("setupSpec() is called")
    }

    def cleanupSpec() {
        println("cleanpuSpec() is called")
    }

    def "test fixture"() {
        println("test()")
        when:
        def result = Integer.parseInt("10")

        then:
        result == 10

        /*
        setupSpec() is called
        setup() is called
        test()
        cleanup() is called
        cleanpuSpec() is called
         */
    }
}
