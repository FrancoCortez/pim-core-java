package com.example.pimcoreapi.channel.application.channel

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import com.example.pimcoreapi.shared.exception.domain.NotFoundException
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException
import lombok.extern.slf4j.Slf4j
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

@Slf4j
class DeleteChannelUserCaseTest extends Specification {

    @Subject
    DeleteChannelUserCase subject
    ChannelServicePort channelServicePort

    void setup () {
        this.channelServicePort = Mock(ChannelServicePortAdapter.class)
        this.subject = new DeleteChannelUserCase(this.channelServicePort)
    }

    def 'should call deleteById method with given id parameter'() {
        given:
        def deleteId = 'someId'
        when:
        this.subject.deleteById(deleteId)
        then:
        1 * channelServicePort.deleteById(_)
    }


    def 'should throw IllegalArgumentException when id parameter is null'() {
        given:
        def deleteId = 'notFoundId'
        when:
        this.subject.deleteById(deleteId)
        then:
        1 * channelServicePort.deleteById(_) >> { throw new NotFoundException(deleteId, 'Channel') }
        and:
        def exception = thrown(Exception)
        exception instanceof NotFoundException
        exception.message == "The Channel object for id notFoundId not found"
    }

}
