package com.sstohnij.stacktraceqabackendv0.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String roleName;

    private String roleDescription;

    @ManyToMany(mappedBy = "roles")
    private Set<AppUser> users;
}
