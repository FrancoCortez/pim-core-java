package com.example.pimcoreapi.shared.userapplication.controller

import com.example.pimcoreapi.shared.exception.base.BusinessException
import com.example.pimcoreapi.shared.exception.base.InfrastructureException
import com.example.pimcoreapi.shared.exception.data.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest
import spock.lang.Specification

class GlobalExceptionHandlerTest extends Specification {
    def "should handle BusinessExceptions and return a ResponseEntity with a BadRequest status code and an ErrorResponse object containing the exception message"() {
        given:
        def ex = new BusinessException("Test Exception")
        def request = Mock(WebRequest)
        when:
        def result = new GlobalExceptionHandler().handleBusinessException(ex, request)
        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        result.body instanceof ErrorResponse
        def error = result.body as ErrorResponse
        error.message == "Validation failed"
        error.errors.contains("Test Exception")
        error.error == "Bad Request"
        error.path == null
        error.status == 400
    }

    def "should return a ResponseEntity with a BAD_REQUEST status code when given an InfrastructureException"() {
        given:
        def ex = new InfrastructureException("Test Exception")
        def request = Mock(WebRequest)
        when:
        def result = new GlobalExceptionHandler().handleInfrastructureException(ex, request)
        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        result.body instanceof ErrorResponse
        def error = result.body as ErrorResponse
        error.message == "Validation failed"
        error.errors.contains("Test Exception")
        error.error == "Bad Request"
        error.path == null
        error.status == 400
    }

    def "should return a ResponseEntity object with an ErrorResponse object and HttpStatus.INTERNAL_SERVER_ERROR status code"() {
        given:
        def ex = new Exception("Test Exception")
        def request = Mock(WebRequest)
        when:
        def result = new GlobalExceptionHandler().handleGenericException(ex, request)
        then:
        result.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        result.body instanceof ErrorResponse
        def error = result.body as ErrorResponse
        error.message == "We are sorry, an internal error has occurred."
        error.errors.contains("Test Exception")
        error.path == null
        error.error == "Internal Server Error"
        error.status == 500
    }
}
