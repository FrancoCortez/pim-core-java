package com.example.pimcoreapi.channel.application.channel;

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateChannelUserCase {
    private final ChannelServicePort channelServicePort;

    public ResourceChannelDto unitCreate(CreateChannelDto channelDto) {
        return this.channelServicePort.create(channelDto);
    }
}
