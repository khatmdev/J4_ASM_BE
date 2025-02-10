package org.example.java4_asm_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "Videos")
public class Video {
	
	@Id
	private String id;
	private String title;
	private long views;
	private String description;
	private boolean active;
	
	@OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Favorite> favorites;
	
	@OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Share> shares;
}
