package com.passGuardian.passGuardian.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "passwords")
@Entity(name = "passwords")
@Getter
@Setter
@EqualsAndHashCode(of = "password_id")
public class PassWordsModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long password_id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 100)
    private String url;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 1000)
    private String password;

    @Column(columnDefinition = "char(1) default 'N'")
    private String favorite;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersModel userId;
}
