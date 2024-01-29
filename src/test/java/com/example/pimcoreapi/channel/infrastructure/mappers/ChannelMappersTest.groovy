package com.example.pimcoreapi.channel.infrastructure.mappers

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto
import com.example.pimcoreapi.channel.infrastructure.entities.Channel
import com.example.pimcoreapi.shared.exception.infreastructure.IsEmptyException
import com.example.pimcoreapi.shared.exception.infreastructure.ObjectNullException
import lombok.extern.slf4j.Slf4j
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Timeout

import java.util.concurrent.TimeUnit

@Slf4j
class ChannelMappersTest extends Specification {

    @Subject
    ChannelMappers subject
    @Shared
    Channel entityMock = new Channel([code: "12345", name: "channel", id: "1"])
    @Shared
    def codeChannelMock = "12345"
    @Shared
    def nameChannelMock = "channel"

    void setup() {
        this.subject = new ChannelMappersImpl()
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "toModelCreate: should mapper channel with valid input and return Channel entity object"() {
        given:
        def createChannelDtoMock = new CreateChannelDto([name: this.nameChannelMock])
        when:
        def result = this.subject.toModelCreate(createChannelDtoMock, this.codeChannelMock)
        then:
        result.name == this.entityMock.name
        result.code == this.entityMock.code
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "toModelCreate: should throw an exception when given a CreateChannelDto is invalid"() {
        given:
        def createChannelDtoMock = inputDto
        when:
        this.subject.toModelCreate(createChannelDtoMock, code)
        then:
        def exception = thrown(expectedException.getClass() as Class<Throwable>)
        exception.message == expectedMessage
        where:
        inputDto                         | code | expectedException                       | expectedMessage
        null                             | ""   | new ObjectNullException("Channel")      | "The object: Channel Created is null"
        new CreateChannelDto()           | ""   | new ObjectNullException("Name")         | "The object: Name is null"
        new CreateChannelDto([name: ""]) | ""   | new IsEmptyException("Name", "Channel") | "The argument: Name is empty for object: Channel Created"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "toModelCreateUpdate: should mapper channel with valid input and return Channel entity object"() {
        given:
        def createChannelDtoMock = new UpdateChannelDto([name: this.nameChannelMock])
        when:
        def result = this.subject.toModelCreateUpdate(createChannelDtoMock, this.codeChannelMock, "1")
        then:
        result.name == this.entityMock.name
        result.code == this.entityMock.code
        result.id == this.entityMock.id
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "toModelCreateUpdate: should throw an exception when given a UpdateCreateChannelDto is invalid"() {
        given:
        def createChannelDtoMock = inputDto
        when:
        this.subject.toModelCreateUpdate(createChannelDtoMock, code, id)
        then:
        def exception = thrown(expectedException.getClass() as Class<Throwable>)
        exception.message == expectedMessage
        where:
        inputDto                              | code | id   | expectedException                               | expectedMessage
        null                                  | ""   | "1"  | new ObjectNullException("Channel Updated")      | "The object: Channel Updated is null"
        new UpdateChannelDto()                | ""   | "1"  | new ObjectNullException("Name")                 | "The object: Name is null"
        new UpdateChannelDto([name: ""])      | ""   | "1"  | new IsEmptyException("Name", "Channel Updated") | "The argument: Name is empty for object: Channel Updated"
        new UpdateChannelDto([name: "Valor"]) | ""   | ""   | new IsEmptyException("Id", "Channel Updated")   | "The argument: Id is empty for object: Channel Updated"
        new UpdateChannelDto([name: "Valor"]) | ""   | null | new IsEmptyException("Id", "Channel Updated")   | "The argument: Id is empty for object: Channel Updated"
    }


    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "toResource: should mapper channel with valid input and return ResourceChannelDto then entity object"() {
        given:
        def resourceChannelDtoMock = new ResourceChannelDto([name: this.nameChannelMock, code: this.codeChannelMock])
        when:
        def result = this.subject.toResource(this.entityMock)
        then:
        result.name == resourceChannelDtoMock.name
        result.code == resourceChannelDtoMock.code
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "toResource: should mapper channel with invalid input null and return null object"() {
        given:
        Channel entityMockObject = null
        when:
        def result = this.subject.toResource(entityMockObject)
        then:
        result == null
    }
}
