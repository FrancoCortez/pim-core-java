package com.example.pimcoreapi.shared.utils

import spock.lang.Specification
import spock.lang.Subject

class UtilsTest extends Specification {
    @Subject
    Utils utils

    void setup() {
        this.utils = new Utils()
    }

    def "should generate code for a single word input"() {
        when:
        def resultInputTest = this.utils.generateCode(inputText)
        then:
        resultInputTest == expetText
        where:
        inputText             | expetText
        "París"               | "paris"
        "Cómodo Coso Testing" | "comodoCosoTesting"
        "Sobrio Loco"         | "sobrioLoco"
    }
}
