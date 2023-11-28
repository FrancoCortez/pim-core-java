package com.example.pimcoreapi.channel.userapplication.controller;

import com.example.pimcoreapi.channel.application.channel.CreateChannelUserCase;
import com.example.pimcoreapi.channel.application.channel.DeleteChannelUserCase;
import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto;
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/channel")
public class ChannelController {
    private final CreateChannelUserCase createChannelUserCase;
    private final DeleteChannelUserCase deleteChannelUserCase;

    @PostMapping
    public ResponseEntity<ResourceChannelDto> create (@Valid @RequestBody CreateChannelDto channelDto) {
        return ResponseEntity.ok(this.createChannelUserCase.unitCreate(channelDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        this.deleteChannelUserCase.deleteById(id);
        return ResponseEntity.ok().build();
    }
}