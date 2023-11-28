package com.example.pimcoreapi.channel.application.channel;

import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateChannelUserCase {
    private final ChannelServicePort channelServicePort;
}
