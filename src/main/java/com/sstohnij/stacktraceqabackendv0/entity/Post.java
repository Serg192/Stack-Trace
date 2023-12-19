package com.sstohnij.stacktraceqabackendv0.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser user;

    @Column(length = 100)
    private String title;

    @Column(length = 1000)
    private String postContent;

    @Temporal(TemporalType.DATE)
    private Date publishDate;

    @ManyToMany
    private Set<Category> categories;

    private boolean problemSolved;

    private boolean postBanned;
}
