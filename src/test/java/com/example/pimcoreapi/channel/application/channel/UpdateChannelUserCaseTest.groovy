package com.example.pimcoreapi.channel.application.channel

import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import lombok.extern.slf4j.Slf4j
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@Slf4j
class UpdateChannelUserCaseTest extends Specification {

    @Subject
    UpdateChannelUserCase subject
    ChannelServicePort channelServicePort

    void setup () {
        this.channelServicePort = Mock(ChannelServicePortAdapter.class)
        this.subject = new UpdateChannelUserCase(this.channelServicePort)
    }
}
