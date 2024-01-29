package com.example.pimcoreapi.channel.infrastructure.adapters;

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto;
import com.example.pimcoreapi.channel.domain.ports.spi.ChannelPersistencePort;
import com.example.pimcoreapi.channel.infrastructure.mappers.ChannelMappers;
import com.example.pimcoreapi.channel.infrastructure.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ChannelPersistencePortAdapter implements ChannelPersistencePort {
    private final ChannelRepository repository;
    private final ChannelMappers mapper;

    public ResourceChannelDto create(CreateChannelDto channelDto, String code) {
        var entity = this.mapper.toModelCreate(channelDto, code);
        var entityResult = this.repository.save(entity);
        return this.mapper.toResource(entityResult);
    }

    public ResourceChannelDto findById(String id) {
        return this.mapper.toResource(this.repository.findById(id).orElse(null));
    }

    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }

    public List<ResourceChannelDto> findAll() {
        return this.repository.findAll().stream()
                .map(this.mapper::toResource)
                .collect(Collectors.toList());
    }

    public ResourceChannelDto updateById(UpdateChannelDto channelDto, String id, String code) {
        var entity = this.mapper.toModelCreateUpdate(channelDto, code, id);
        return this.mapper.toResource(this.repository.save(entity));
    }
}
