package com.example.pimcoreapi.channel.infrastructure.entities;

import com.example.pimcoreapi.shared.infrastructure.entity.BaseIdEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
        @Index(name = "idx_channel_code", columnList = "code")
})
public class Channel extends BaseIdEntity {
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "code", length = 50, nullable = false)
    private String code;
}
