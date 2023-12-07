package com.example.pimcoreapi.channel.domain.ports.spi

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.infrastructure.adapters.ChannelPersistencePortAdapter
import com.example.pimcoreapi.channel.infrastructure.entities.Channel
import com.example.pimcoreapi.channel.infrastructure.mappers.ChannelMappers
import com.example.pimcoreapi.channel.infrastructure.mappers.ChannelMappersImpl
import com.example.pimcoreapi.channel.infrastructure.repository.ChannelRepository
import lombok.extern.slf4j.Slf4j
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Timeout

import java.util.concurrent.TimeUnit

@Slf4j
class ChannelPersistencePortTest extends Specification {

    @Subject
    ChannelPersistencePort subject
    ChannelRepository repository
    ChannelMappers mapper

    void setup() {
        this.mapper = Mock(ChannelMappersImpl.class)
        this.repository = Mock(ChannelRepository.class)
        this.subject = new ChannelPersistencePortAdapter(repository, mapper)
    }

    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    def "create: should create channel with valid input and return ResourceChannelDto object"() {
        given:
        def name = "Test Channel"
        def code = "12345"
        def channelDto = new CreateChannelDto([name: name])
        def entityResult = new Channel([id: "id", name: name, code: code])
        def expectResult = new ResourceChannelDto([id: "id", name: name, code: code])
        when:
        def result = subject.create(channelDto, code)
        then:
        1 * this.mapper.toModelCreate(_, _) >> entityResult
        1 * this.repository.save(_) >> entityResult
        1 * this.mapper.toResource(_) >> expectResult
        and:
        result instanceof ResourceChannelDto
        verifyAll(result) {
            id == expectResult.id
            name == expectResult.name
            code == expectResult.code
        }
    }

    def "findById: should return ResourceChannelDto when a valid id is passed"() {
        given:
        def id = "validId"
        def expectedResourceChannelDto = new ResourceChannelDto()
        def channelMock = new Channel()
        when:
        def result = subject.findById(id)
        then:
        1 * this.repository.findById(_ as String) >> Optional.of(channelMock)
        1 * this.mapper.toResource(_ as Channel) >> expectedResourceChannelDto
        and:
        result == expectedResourceChannelDto
    }

    def "findById: should return null when id is null"() {
        given:
        def id = "invalid"
        when:
        def result = subject.findById(id)
        then:
        1 * this.repository.findById(_ as String) >> Optional.ofNullable(null)
        and:
        result == null
    }

    def "deleteById: should delete an existing channel with a valid id"() {
        given:
        def id = "validId"
        when:
        subject.deleteById(id)
        then:
        1 * repository.deleteById(id)
    }

    def "deleteAll: should delete an existing all channel"() {
        when:
        subject.deleteAll()
        then:
        1 * repository.deleteAll()
    }

    def "findAll: should return List ResourceChannelDto"() {
        given:
        def expectedResponse = [
                new ResourceChannelDto([name: 'Test', code: 'test', id: 'id']),
                new ResourceChannelDto([name: 'Test1', code: 'test1', id: 'id1']),
                new ResourceChannelDto([name: 'Test2', code: 'test2', id: 'id2'])
        ]
        def mockEntity = [
                new Channel([name: 'Test', code: 'test', id: 'id']),
                new Channel([name: 'Test1', code: 'test1', id: 'id1']),
                new Channel([name: 'Test2', code: 'test2', id: 'id2'])
        ]
        when:
        def result = subject.findAll()
        then:
        1 * this.repository.findAll() >> mockEntity
        mockEntity.eachWithIndex { entity, index ->
            1 * this.mapper.toResource(entity) >> expectedResponse[index]
        }

        and:
        result.size() == expectedResponse.size()
        expectedResponse.eachWithIndex { expected, index ->
            result[index].name == expected.name
            result[index].code == expected.code
            result[index].id == expected.id
        }
    }

}
