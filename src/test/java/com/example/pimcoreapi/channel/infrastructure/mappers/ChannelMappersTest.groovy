package com.example.pimcoreapi.channel.infrastructure.mappers

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.infrastructure.entities.Channel
import com.example.pimcoreapi.shared.exception.infreastructure.IsEmptyException
import com.example.pimcoreapi.shared.exception.infreastructure.ObjectNullException
import lombok.extern.slf4j.Slf4j
import org.springframework.boot.test.context.SpringBootTest
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
    Channel entityMock = new Channel([code: "12345", name: "channel"])
    @Shared
    def codeChannelMock = "12345"
    @Shared
    def nameChannelMock = "channel"

    void setup() {
        this.subject = new ChannelMappersImpl()
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should mapper channel with valid input and return Channel entity object"() {
        given:
        def createChannelDtoMock = new CreateChannelDto([ name: this.nameChannelMock])
        when:
        def result = this.subject.toModelCreate(createChannelDtoMock, this.codeChannelMock)
        then:
        result.name == this.entityMock.name
        result.code == this.entityMock.code
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should throw an exception when given a CreateChannelDto with a null"() {
        given:
        def createChannelDtoMock = null
        when:
        this.subject.toModelCreate(createChannelDtoMock, "")
        then:
        def exception = thrown(Exception)
        exception instanceof ObjectNullException
        exception.message == "The object: Channel Created is null"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should throw an exception when given a CreateChannelDto with a name null"() {
        given:
        def createChannelDtoMock = new CreateChannelDto()
        when:
        this.subject.toModelCreate(createChannelDtoMock, "")
        then:
        def exception = thrown(Exception)
        exception instanceof ObjectNullException
        exception.message == "The object: Name is null"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should throw an exception when given a CreateChannelDto with a name is empty"() {
        given:
        def createChannelDtoMock = new CreateChannelDto([ name: ""])
        when:
        this.subject.toModelCreate(createChannelDtoMock, "")
        then:
        def exception = thrown(Exception)
        exception instanceof IsEmptyException
        exception.message == "The argument: Name is empty for object: Channel Created"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should mapper channel with valid input and return ResourceChannelDto then entity object"() {
        given:
        def resourceChannelDtoMock = new ResourceChannelDto([ name: this.nameChannelMock, code: this.codeChannelMock])
        when:
        def result = this.subject.toResource(this.entityMock)
        then:
        result.name == resourceChannelDtoMock.name
        result.code == resourceChannelDtoMock.code
    }
}
