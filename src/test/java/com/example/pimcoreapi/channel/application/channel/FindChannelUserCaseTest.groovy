package com.example.pimcoreapi.channel.application.channel

import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import com.example.pimcoreapi.shared.exception.domain.IsEmptyException
import com.example.pimcoreapi.shared.exception.domain.NotFoundException
import lombok.extern.slf4j.Slf4j
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

@Slf4j
class FindChannelUserCaseTest extends Specification {

    @Subject
    FindChannelUserCase subject
    ChannelServicePort channelServicePort

    void setup() {
        this.channelServicePort = Mock(ChannelServicePortAdapter.class)
        this.subject = new FindChannelUserCase(this.channelServicePort)
    }

    def 'findById: should call findById method with given id parameter'() {
        given:
        def someId = 'someId'
        ResourceChannelDto expectedResourceChannelDto = new ResourceChannelDto(id: '1', name: 'Test Channel', code: 'TC', createdBy: 'Test', createdDate: LocalDateTime.now(), lastModifiedBy: 'Test', lastModifiedDate: LocalDateTime.now())
        when:
        def result = this.subject.findById(someId)
        then:
        1 * channelServicePort.findById(_) >> expectedResourceChannelDto
        and:
        verifyAll(result) {
            id == expectedResourceChannelDto.id
            code == expectedResourceChannelDto.code
            name == expectedResourceChannelDto.name
            createdBy == expectedResourceChannelDto.createdBy
            lastModifiedBy == expectedResourceChannelDto.lastModifiedBy
            createdDate == expectedResourceChannelDto.createdDate
            lastModifiedDate == expectedResourceChannelDto.lastModifiedDate
        }
    }

    def 'findById: should call findById method with given invalid id parameter null IsEmptyException'() {
        given:
        def someId = 'null'
        when:
        this.subject.findById(someId)
        then:
        1 * channelServicePort.findById(_) >> { throw new IsEmptyException("id", "Search Channel") }
        and:
        def exception = thrown(Exception)
        exception instanceof IsEmptyException
        exception.message == "The argument: id is empty for object: Search Channel"
    }

    def 'findById: should call findById method with given invalid id parameter null '() {
        given:
        def someId = 'notFoundId'
        when:
        this.subject.findById(someId)
        then:
        1 * channelServicePort.findById(_) >> { throw new NotFoundException(someId, "Channel") }
        and:
        def exception = thrown(Exception)
        exception instanceof NotFoundException
        exception.message == "The Channel object for id notFoundId not found"
    }
}
