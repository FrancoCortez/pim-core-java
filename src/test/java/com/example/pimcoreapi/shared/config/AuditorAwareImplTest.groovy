package com.example.pimcoreapi.shared.config

import spock.lang.Specification

class AuditorAwareImplTest extends Specification {

    def "should return an empty Optional when the current auditor's name is an empty string"() {
        given:
        AuditorAwareImpl auditorAware = new AuditorAwareImpl()
        when:
        Optional<String> result = auditorAware.getCurrentAuditor()
        then:
        !result.isPresent()
    }
}
