package com.kobi.elearning.entity;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	String title;
	String description;
	@ManyToMany
	@JoinTable (name = "course_user" ,
			joinColumns = @JoinColumn (name = "course_id") ,
			inverseJoinColumns = @JoinColumn (name = "user_id")
	)
	List<User> users;
	@OneToMany (mappedBy = "course" , cascade = CascadeType.ALL, orphanRemoval = true)
	List<Section> sections;
	LocalDate startDate;
	LocalDate endDate;
}
