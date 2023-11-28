package com.example.pimcoreapi.channel.application.channel;

import com.example.pimcoreapi.channel.domain.ports.api.ChannelServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindChannelUserCase {
    private final ChannelServicePort channelServicePort;
}
