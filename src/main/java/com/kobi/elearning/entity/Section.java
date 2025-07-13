package com.kobi.elearning.entity;

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
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	String title;
	String content;
	String videoUrl;
	String description;
	@ManyToOne
	Course course;
}
