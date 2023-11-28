package com.example.pimcoreapi.channel.application.channel

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException
import lombok.extern.slf4j.Slf4j
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime
import java.time.ZonedDateTime


@Slf4j
class CreateChannelUserCaseTest extends Specification {

    @Subject
    CreateChannelUserCase subject
    ChannelServicePort channelServicePort

    void setup() {
        this.channelServicePort = Mock(ChannelServicePortAdapter.class)
        this.subject = new CreateChannelUserCase(this.channelServicePort)
    }

    def 'should return ResourceChannelDto object when valid CreateChannelDto is passed as argument'() {
        given:
        CreateChannelDto channelDto = new CreateChannelDto(name: 'Test Channel')
        ResourceChannelDto expectedResourceChannelDto = new ResourceChannelDto(id: '1', name: 'Test Channel', code: 'TC', createdBy: 'Test', createdDate: LocalDateTime.now(), lastModifiedBy: 'Test', lastModifiedDate: LocalDateTime.now())
        when:
        ResourceChannelDto result = this.subject.unitCreate(channelDto)
        then:
        1 * channelServicePort.create(channelDto) >> expectedResourceChannelDto
        and:
        result != null
        result == expectedResourceChannelDto
    }

    def 'should return ResourceChannelDto object when valid CreateChannelDto is passed as error'() {
        given:
        CreateChannelDto channelDto = new CreateChannelDto(name: 'Test Channel')
        when:
        this.subject.unitCreate(channelDto)
        then:
        channelServicePort.create(channelDto) >> { throw new ObjectNullException('Name') }
        def exception = thrown(Exception)
        exception instanceof ObjectNullException
        exception.message == "The object: Name is null"
    }
}
