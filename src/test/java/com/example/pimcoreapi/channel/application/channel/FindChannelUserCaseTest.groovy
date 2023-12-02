package com.example.pimcoreapi.channel.application.channel

import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort
import com.example.pimcoreapi.channel.domain.service.ChannelServicePortAdapter
import lombok.extern.slf4j.Slf4j
import spock.lang.Specification
import spock.lang.Subject

@Slf4j
class FindChannelUserCaseTest extends Specification {

    @Subject
    FindChannelUserCase subject
    ChannelServicePort channelServicePort

    void setup() {
        this.channelServicePort = Mock(ChannelServicePortAdapter.class)
        this.subject = new FindChannelUserCase(this.channelServicePort)
    }
}
