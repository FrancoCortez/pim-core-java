package com.example.pimcoreapi.shared.config

import spock.lang.Specification

class AuditorAwareImplTest extends Specification {

    def "should return an empty Optional when the current auditors name is an empty string"() {
        given:
        def auditorAware = new AuditorAwareImpl()
        when:
        def result = auditorAware.getCurrentAuditor()
        then:
        !result.isPresent()
    }
}
