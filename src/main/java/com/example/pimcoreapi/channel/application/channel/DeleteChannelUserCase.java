package com.example.pimcoreapi.channel.application.channel;

import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteChannelUserCase {
    private final ChannelServicePort channelServicePort;

    public void deleteById(String id) {
        this.channelServicePort.deleteById(id);
    }

    public void deleteAll() {
        this.channelServicePort.deleteAll();
    }
}
