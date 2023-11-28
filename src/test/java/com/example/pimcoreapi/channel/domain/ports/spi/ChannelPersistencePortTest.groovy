package com.example.pimcoreapi.channel.domain.ports.spi

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.infrastructure.adapters.ChannelPersistencePortAdapter
import com.example.pimcoreapi.channel.infrastructure.entities.Channel
import com.example.pimcoreapi.channel.infrastructure.mappers.ChannelMappers
import com.example.pimcoreapi.channel.infrastructure.mappers.ChannelMappersImpl
import com.example.pimcoreapi.channel.infrastructure.repository.ChannelRepository
import lombok.extern.slf4j.Slf4j
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

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

    def "should create channel with valid input and return ResourceChannelDto object"() {
        given:
        def channelDto = CreateChannelDto.builder()
                .name("Test Channel")
                .build()
        def code = "12345"
        def entityResult = new Channel();
        entityResult.setId('id')
        entityResult.setName("Test Channel")
        entityResult.setCode("12345")
        def expectResult = ResourceChannelDto.builder()
                .id("id")
                .name("Test Channel")
                .code("12345")
                .build()
        when:
        def result = subject.create(channelDto, code)
        then:
        1 * this.mapper.toModelCreate(_, _) >> entityResult
        1 * this.repository.save(_) >> entityResult
        1 * this.mapper.toResource(_) >> expectResult
        and:
        result instanceof ResourceChannelDto
        result.id == expectResult.id
        result.name == expectResult.name
        result.code == expectResult.code
    }

}
