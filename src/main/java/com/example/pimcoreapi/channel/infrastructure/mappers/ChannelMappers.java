package com.example.pimcoreapi.channel.infrastructure.mappers;

import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto;
import com.example.pimcoreapi.channel.infrastructure.entities.Channel;
import com.example.pimcoreapi.shared.exception.infreastructure.IsEmptyException;
import com.example.pimcoreapi.shared.exception.infreastructure.ObjectNullException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMappers {

    default Channel toModelCreate(CreateChannelDto dto, String code) {
        if (dto == null) throw new ObjectNullException("Channel Created");
        if (dto.getName() == null) throw new ObjectNullException("Name");
        if (dto.getName().isEmpty() || dto.getName().isBlank()) throw new IsEmptyException("Name", "Channel Created");
        Channel entity = new Channel();
        entity.setName(dto.getName());
        entity.setCode(code);
        return entity;
    }

    default Channel toModelCreateUpdate(UpdateChannelDto dto, String code, String id) {
        if (dto == null) throw new ObjectNullException("Channel Updated");
        if (dto.getName() == null) throw new ObjectNullException("Name");
        if (dto.getName().isEmpty()) throw new IsEmptyException("Name", "Channel Updated");
        if (id == null || id.isEmpty()) throw new IsEmptyException("Id", "Channel Updated");
        Channel entity = new Channel();
        entity.setName(dto.getName());
        entity.setCode(code);
        entity.setId(id);
        return entity;
    }

    ResourceChannelDto toResource(Channel entity);
}
