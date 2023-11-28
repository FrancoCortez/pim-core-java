package com.example.pimcoreapi.channel.infrastructure.repository;

import com.example.pimcoreapi.channel.infrastructure.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {


}
