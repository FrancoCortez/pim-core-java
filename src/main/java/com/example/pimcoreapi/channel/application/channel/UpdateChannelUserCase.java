package com.example.pimcoreapi.channel.application.channel;

import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto;
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateChannelUserCase {
    private final ChannelServicePort channelServicePort;

    public ResourceChannelDto updateById(UpdateChannelDto channelDto, String id) {
        return this.channelServicePort.updateById(channelDto, id);
    }
}
