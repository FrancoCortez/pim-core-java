package com.example.pimcoreapi.channel.domain.ports.api;

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;

public interface ChannelServicePort {

    ResourceChannelDto create(CreateChannelDto channelDto);

    void deleteById(String id);

    void deleteAll();

    ResourceChannelDto findById(String id);
}
