package com.example.pimcoreapi.channel.application.channel


import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import com.example.pimcoreapi.shared.exception.domain.IsEmptyException
import com.example.pimcoreapi.shared.exception.domain.NotFoundException
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException
import lombok.extern.slf4j.Slf4j
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

@Slf4j
class UpdateChannelUserCaseTest extends Specification {

    @Subject
    UpdateChannelUserCase subject
    ChannelServicePort channelServicePort

    void setup() {
        this.channelServicePort = Mock(ChannelServicePortAdapter.class)
        this.subject = new UpdateChannelUserCase(this.channelServicePort)
    }

    def "should return ResourceChannelDto object when valid UpdateChannelDto is passed as argument"() {
        given:
        def channelDto = new UpdateChannelDto(name: "Test Channel")
        def id = "1"
        def expectedResourceChannelDto = new ResourceChannelDto(id: id, name: "Test Channel", code: "TC", createdBy: "Test", createdDate: LocalDateTime.now(), lastModifiedBy: "Test", lastModifiedDate: LocalDateTime.now())
        when:
        def result = this.subject.updateById(channelDto, id)
        then:
        1 * channelServicePort.updateById(_, _) >> expectedResourceChannelDto
        and:
        result != null
        result == expectedResourceChannelDto
    }

    def "should return exception when invalid input is passed as error"() {
        given:
        def channelDto = inputDto
        def id = idMock
        when:
        this.subject.updateById(channelDto, id)
        then:
        this.channelServicePort.updateById(channelDto, id) >> { throw expectedException }
        def exception = thrown(expectedException.getClass() as Class<Throwable>)
        exception.message == expectedMessage
        where:
        inputDto                                | expectedException                               | expectedMessage                                           | idMock
        new UpdateChannelDto(name: null)        | new ObjectNullException("Name")                 | "The object: Name is null"                                | "1"
        null                                    | new ObjectNullException("Channel Created")      | "The object: Channel Created is null"                     | "1"
        new UpdateChannelDto(name: "name")      | new IsEmptyException("Name", "Created Channel") | "The argument: Name is empty for object: Created Channel" | "1"
        new UpdateChannelDto(name: "null name") | new IsEmptyException("Id", "Search Channel")    | "The argument: Id is empty for object: Search Channel"    | null
        new UpdateChannelDto(name: "test")      | new NotFoundException("1", "Channel")           | "The Channel object for id 1 not found"                   | "1"
    }


}
