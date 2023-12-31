package com.example.pimcoreapi.channel.application.channel;

import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindChannelUserCase {
    private final ChannelServicePort channelServicePort;

    public ResourceChannelDto findById(String id) {
        return this.channelServicePort.findById(id);
    }

    public List<ResourceChannelDto> findAll() {
        return this.channelServicePort.findAll();
    }
}
