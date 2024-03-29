package com.example.pimcoreapi.channel.domain.service;

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto;
import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort;
import com.example.pimcoreapi.channel.domain.ports.spi.ChannelPersistencePort;
import com.example.pimcoreapi.shared.exception.domain.IsEmptyException;
import com.example.pimcoreapi.shared.exception.domain.NotFoundException;
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException;
import com.example.pimcoreapi.shared.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelServicePortAdapter implements ChannelServicePort {

    private final ChannelPersistencePort channelPersistencePort;
    private final Utils utils;

    public ResourceChannelDto create(CreateChannelDto channelDto) {
        this.validateCreateInput(channelDto);
        String code = this.utils.generateCode(channelDto.getName());
        return this.channelPersistencePort.create(channelDto, code);
    }

    public void deleteById(String id) {
        if (id == null || id.isEmpty()) throw new IsEmptyException("id", "Channel");
        ResourceChannelDto channelDto = this.channelPersistencePort.findById(id);
        if (channelDto == null) throw new NotFoundException(id, "Channel");
        this.channelPersistencePort.deleteById(id);
    }

    public void deleteAll() {
        this.channelPersistencePort.deleteAll();
    }

    public ResourceChannelDto findById(String id) {
        if (id.isEmpty() || id.isBlank()) throw new IsEmptyException("id", "Search Channel");
        var channelDto = this.channelPersistencePort.findById(id);
        if (channelDto == null) throw new NotFoundException(id, "Channel");
        return channelDto;
    }

    public List<ResourceChannelDto> findAll() {
        return this.channelPersistencePort.findAll();
    }

    public ResourceChannelDto updateById(UpdateChannelDto channelDto, String id) {
        validateUpdateInput(channelDto, id);
        var findChannelDto = this.channelPersistencePort.findById(id);
        if (findChannelDto == null) throw new NotFoundException(id, "Channel");
        var code = this.utils.generateCode(channelDto.getName());
        return this.channelPersistencePort.updateById(channelDto, id, code);
    }

    private void validateCreateInput(CreateChannelDto channelDto) {
        if (channelDto == null) throw new ObjectNullException("Channel Created");
        if (channelDto.getName() == null) throw new ObjectNullException("Name");
        if (channelDto.getName().isEmpty() || channelDto.getName().isBlank())
            throw new IsEmptyException("Name", "Created Channel");
    }

    private void validateUpdateInput(UpdateChannelDto channelDto, String id) {
        if (channelDto == null) throw new ObjectNullException("Channel Created");
        if (channelDto.getName() == null) throw new ObjectNullException("Name");
        if (channelDto.getName().isEmpty() || channelDto.getName().isBlank())
            throw new IsEmptyException("Name", "Created Channel");
        if (id.isEmpty() || id.isBlank()) throw new IsEmptyException("id", "Search Channel");
    }
}
