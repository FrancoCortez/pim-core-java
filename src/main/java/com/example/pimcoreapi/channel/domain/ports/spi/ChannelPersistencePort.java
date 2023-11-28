package com.example.pimcoreapi.channel.domain.ports.spi;

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;

public interface ChannelPersistencePort {
    ResourceChannelDto create(CreateChannelDto channelDto, String code);

    ResourceChannelDto findById(String id);

    void deleteById(String id);
}
