package com.example.pimcoreapi

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PimCoreApiApplicationTest extends Specification {

    def "should run without errors or exceptions when passed valid arguments"() {
        given:
        String[] args = ["arg1", "arg2"]
        when:
        PimCoreApiApplication.main(args)
        then:
        noExceptionThrown()
    }
}
