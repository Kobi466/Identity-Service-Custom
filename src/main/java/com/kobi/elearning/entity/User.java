package com.kobi.elearning.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;
	@Column(unique = true, nullable = false)
	String userName;
	String passWord;
	String fullName;
//    Long yearOfBirth;
//    Sex sex;
//    String bio;
//    @Column(unique = true, nullable = false)
//    String email;
//    @Column(unique = true, nullable = false)
//    String phoneNumber;
//    String avatar;
	@ManyToMany
	Set<Role> roles;


}
