package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.OffsetDateTime;


@Entity
@Table(name = "short_links")
@Getter
@Setter
public class ShortLink {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "creation_date", nullable = false)
    private OffsetDateTime creationDate;

    @Column(name = "expiry_date")
    private OffsetDateTime expiryDate;

    @Column(name = "status", nullable = false)
    @Formula("(expiry_date IS NULL OR expiry_date >= CURRENT_TIMESTAMP)")
    private boolean active;
//    @Column(name = "status", nullable = false)
//    private boolean active;

    @Column(name = "clicks", nullable = false)
    private int clicks;

    @PrePersist
    public void prePersist() {
        this.creationDate = OffsetDateTime.now();
    }

}
