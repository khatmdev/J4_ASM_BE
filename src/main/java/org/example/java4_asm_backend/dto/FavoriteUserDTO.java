package org.example.java4_asm_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteUserDTO {
    private String username;
    private String fullname;
    private String email;
    private Date favoriteDate;
}
