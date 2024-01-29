package com.example.pimcoreapi.channel.application.channel


import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import com.example.pimcoreapi.shared.exception.domain.IsEmptyException
import com.example.pimcoreapi.shared.exception.domain.NotFoundException
import lombok.extern.slf4j.Slf4j
import spock.lang.Specification
import spock.lang.Subject

@Slf4j
class DeleteChannelUserCaseTest extends Specification {

    @Subject
    DeleteChannelUserCase subject
    ChannelServicePort channelServicePort

    void setup() {
        this.channelServicePort = Mock(ChannelServicePortAdapter.class)
        this.subject = new DeleteChannelUserCase(this.channelServicePort)
    }

    def "should call deleteById method with given id parameter"() {
        given:
        def deleteId = "someId"
        when:
        this.subject.deleteById(deleteId)
        then:
        1 * channelServicePort.deleteById(_)
    }


    def "should throw IllegalArgumentException when id parameter is null"() {
        given:
        def deleteId = "notFoundId"
        when:
        this.subject.deleteById(deleteId)
        then:
        1 * channelServicePort.deleteById(_) >> { throw new NotFoundException(deleteId, "Channel") }
        and:
        def exception = thrown(Exception)
        exception instanceof NotFoundException
        exception.message == "The Channel object for id notFoundId not found"
    }

    def "should throw IsEmptyException when id parameter is null"() {
        given:
        def deleteId = "notFoundId"
        when:
        this.subject.deleteById(deleteId)
        then:
        1 * channelServicePort.deleteById(_) >> { throw new IsEmptyException("id", "delete Channel") }
        and:
        def exception = thrown(Exception)
        exception instanceof IsEmptyException
        exception.message == "The argument: id is empty for object: delete Channel"
    }

    def "should call deleteAll method with given id parameter"() {
        when:
        this.subject.deleteAll()
        then:
        1 * channelServicePort.deleteAll()
    }

}
