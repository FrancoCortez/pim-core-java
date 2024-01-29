package com.example.pimcoreapi.channel.domain.ports.api

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto
import com.example.pimcoreapi.channel.domain.ports.spi.ChannelPersistencePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import com.example.pimcoreapi.channel.infrastructure.adapters.ChannelPersistencePortAdapter
import com.example.pimcoreapi.shared.exception.domain.IsEmptyException
import com.example.pimcoreapi.shared.exception.domain.NotFoundException
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException
import com.example.pimcoreapi.shared.utils.Utils
import groovy.util.logging.Slf4j
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
    def "create: should generate code and call create method with correct parameters"() {
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
    def "create: should throw an exception when given a CreateChannelDto with a error"() {
        given:
        def createChannelDtoMock = channel
        when:
        this.subject.create(createChannelDtoMock)
        then:
        def exception = thrown(expectedException.getClass() as Class<Throwable>)
        exception.message == message
        where:
        channel                          | expectedException                               | message
        null                             | new ObjectNullException("Channel")              | "The object: Channel Created is null"
        new CreateChannelDto()           | new ObjectNullException("Name")                 | "The object: Name is null"
        new CreateChannelDto([name: ""]) | new IsEmptyException("Name", "Created Channel") | "The argument: Name is empty for object: Created Channel"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "deleteById: should delete a channel with a valid id"() {
        given:
        def id = "validId"
        def channelDto = new ResourceChannelDto(id: id, name: "Test Channel", code: "TC", createdBy: "User", createdDate: LocalDateTime.now(), lastModifiedBy: "User", lastModifiedDate: LocalDateTime.now())
        when:
        this.subject.deleteById(id)
        then:
        1 * channelPersistencePortMock.findById(id) >> channelDto
        1 * channelPersistencePortMock.deleteById(id)
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "deleteById: should delete a channel with a null id"() {
        given:
        def id = null
        when:
        this.subject.deleteById(id)
        then:
        def exception = thrown(Exception)
        exception instanceof IsEmptyException
        exception.message == "The argument: id is empty for object: Channel"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "deleteById: should delete a channel with a invalid id"() {
        given:
        def id = "Not Valid Id"
        when:
        this.subject.deleteById(id)
        then:
        1 * channelPersistencePortMock.findById(id) >> null
        and:
        def exception = thrown(Exception)
        exception instanceof NotFoundException
        exception.message == "The Channel object for id Not Valid Id not found"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "deleteAll: should delete all channel"() {
        when:
        this.subject.deleteAll()
        then:
        1 * channelPersistencePortMock.deleteAll()
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "findById: should find a channel with a valid id"() {
        given:
        def id = "validId"
        def expectDto = new ResourceChannelDto([id: "validId", code: "code", name: "name", createdBy: "legacy", lastModifiedBy: "legacy", createdDate: LocalDateTime.now(), lastModifiedDate: LocalDateTime.now()])
        when:
        def result = this.subject.findById(id)
        then:
        1 * channelPersistencePortMock.findById(id) >> expectDto
        and:
        verifyAll(result) {
            id == expectDto.id
            code == expectDto.code
            name == expectDto.name
            createdBy == expectDto.createdBy
            lastModifiedBy == expectDto.lastModifiedBy
            lastModifiedDate == expectDto.lastModifiedDate
            createdDate == expectDto.createdDate
        }

    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "findById: should find a channel with a empty id"() {
        given:
        def id = ""
        when:
        this.subject.findById(id)
        then:
        def exception = thrown(Exception)
        exception instanceof IsEmptyException
        exception.message == "The argument: id is empty for object: Search Channel"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "findById: should find a channel with a invalid id"() {
        given:
        def id = "invalid"
        when:
        this.subject.findById(id)
        then:
        1 * channelPersistencePortMock.findById(id) >> null
        and:
        def exception = thrown(Exception)
        exception instanceof NotFoundException
        exception.message == "The Channel object for id invalid not found"
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "findAll: should find all a channel"() {
        given:
        def expectedResponse = [
                new ResourceChannelDto([name: "Test", code: "test", id: "id"]),
                new ResourceChannelDto([name: "Test1", code: "test1", id: "id1"]),
                new ResourceChannelDto([name: "Test2", code: "test2", id: "id2"])
        ]
        when:
        def result = this.subject.findAll()
        then:
        1 * channelPersistencePortMock.findAll() >> expectedResponse
        and:
        result.size() == expectedResponse.size()
        expectedResponse.eachWithIndex { expected, index ->
            result[index].name == expected.name
            result[index].code == expected.code
            result[index].id == expected.id
        }

    }

    def "updateById: should update one channel with valid input"() {
        given:
        def channelDto = new UpdateChannelDto(name: "Test Channel")
        def id = "1"
        def expectedResourceChannelDto = new ResourceChannelDto(id: id, name: "Test Channel", code: "TC", createdBy: "Test", createdDate: LocalDateTime.now(), lastModifiedBy: "Test", lastModifiedDate: LocalDateTime.now())
        when:
        def result = this.subject.updateById(channelDto, id)
        then:
        1 * channelPersistencePortMock.findById(id) >> expectedResourceChannelDto
        1 * utilsMock.generateCode(_) >> "TC"
        1 * channelPersistencePortMock.updateById(_, _, _) >> expectedResourceChannelDto
        and:
        result != null
        result == expectedResourceChannelDto
    }

    def "updateById: should update one channel with invalid and throw exception"() {
        given:
        def channelDto = inputDto
        def id = idMock
        when:
        this.subject.updateById(channelDto, id)
        then:
        def exception = thrown(expectedException.getClass() as Class<Throwable>)
        exception.message == expectedMessage
        where:
        inputDto                           | expectedException                               | expectedMessage                                           | idMock
        new UpdateChannelDto(name: null)   | new ObjectNullException("Name")                 | "The object: Name is null"                                | "1"
        null                               | new ObjectNullException("Channel Created")      | "The object: Channel Created is null"                     | "1"
        new UpdateChannelDto(name: "")     | new IsEmptyException("Name", "Created Channel") | "The argument: Name is empty for object: Created Channel" | "1"
        new UpdateChannelDto(name: "test") | new IsEmptyException("id", "Search Channel")    | "The argument: id is empty for object: Search Channel"    | ""
        new UpdateChannelDto(name: "test") | new NotFoundException("1", "Channel")           | "The Channel object for id 1 not found"                   | "1"

    }
}
