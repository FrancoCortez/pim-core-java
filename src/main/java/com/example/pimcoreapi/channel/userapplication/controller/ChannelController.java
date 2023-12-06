package com.example.pimcoreapi.channel.userapplication.controller;

import com.example.pimcoreapi.channel.application.channel.CreateChannelUserCase;
import com.example.pimcoreapi.channel.application.channel.DeleteChannelUserCase;
import com.example.pimcoreapi.channel.application.channel.FindChannelUserCase;
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
    private final FindChannelUserCase findChannelUserCase;

    @PostMapping
    public ResponseEntity<ResourceChannelDto> create(@Valid @RequestBody CreateChannelDto channelDto) {
        return ResponseEntity.ok(this.createChannelUserCase.unitCreate(channelDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        this.deleteChannelUserCase.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all/delete")
    public ResponseEntity<Void> deleteAll() {
        this.deleteChannelUserCase.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceChannelDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(this.findChannelUserCase.findById(id));
    }
}
