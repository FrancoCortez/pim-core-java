package com.example.pimcoreapi.channel.domain.ports.spi;

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto;

import java.util.List;

public interface ChannelPersistencePort {
    ResourceChannelDto create(CreateChannelDto channelDto, String code);

    ResourceChannelDto findById(String id);

    void deleteById(String id);

    void deleteAll();

    List<ResourceChannelDto> findAll();

    ResourceChannelDto updateById(UpdateChannelDto channelDto, String id, String code);

}
