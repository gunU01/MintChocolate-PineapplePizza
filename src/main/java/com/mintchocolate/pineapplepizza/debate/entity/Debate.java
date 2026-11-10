package com.mintchocolate.pineapplepizza.debate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "debates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Debate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "option_a", nullable = false, length = 50)
    private String optionA;

    @Column(name = "option_b", nullable = false, length = 50)
    private String optionB;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Debate(String title, String description, String optionA, String optionB) {
        this.title = title;
        this.description = description;
        this.optionA = optionA;
        this.optionB = optionB;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
