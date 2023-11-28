package com.example.pimcoreapi.channel.domain.ports.api

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.ports.spi.ChannelPersistencePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import com.example.pimcoreapi.channel.infrastructure.adapters.ChannelPersistencePortAdapter
import com.example.pimcoreapi.shared.exception.domain.IsEmptyException
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException
import com.example.pimcoreapi.shared.utils.Utils
import groovy.util.logging.Slf4j
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Timeout

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Slf4j
class ChannelServicePortTest extends Specification {

    @Subject
    ChannelServicePort subject
    ChannelPersistencePort channelPersistencePortMock
    Utils utilsMock

    void setup() {
        this.channelPersistencePortMock = Mock(ChannelPersistencePortAdapter.class)
        this.utilsMock = Mock(Utils.class)
        this.subject = new ChannelServicePortAdapter(this.channelPersistencePortMock, utilsMock)
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should generate code and call create method with correct parameters"() {
        given:
        def channelDto = CreateChannelDto.builder()
                .name("Test Channel")
                .build()
        def generatedCode = "testChannel"
        def expectedResourceChannelDto = ResourceChannelDto.builder()
                .name("Test Channel")
                .code(generatedCode)
                .build()
        when:
        def result = this.subject.create(channelDto)
        then:
        1 * this.utilsMock.generateCode(channelDto.getName()) >> generatedCode
        1 * this.channelPersistencePortMock.create(channelDto, generatedCode) >> expectedResourceChannelDto
        and:
        result == expectedResourceChannelDto
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should throw an exception when given a CreateChannelDto with a null"() {
        given:
        def createChannelDtoMock = null
        when:
        this.subject.create(createChannelDtoMock)
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
        this.subject.create(createChannelDtoMock)
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
        this.subject.create(createChannelDtoMock)
        then:
        def exception = thrown(Exception)
        exception instanceof IsEmptyException
        exception.message == "The argument: Name is empty for object: Created Channel"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "should delete a channel with a valid id"() {
        given:
        def id = "validId"
        def channelDto = new ResourceChannelDto(id: id, name: "Test Channel", code: "TC", createdBy: "User", createdDate: LocalDateTime.now(), lastModifiedBy: "User", lastModifiedDate: LocalDateTime.now())
        channelPersistencePortMock.findById(_ as String) >> channelDto
        when:
        this.subject.deleteById(id)
        then:
        1 * channelPersistencePortMock.findById(id) >> channelDto
        1 * channelPersistencePortMock.deleteById(id)
    }
}
