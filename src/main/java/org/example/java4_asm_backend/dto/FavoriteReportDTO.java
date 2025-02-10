package org.example.java4_asm_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteReportDTO {
    private String videoTitle;
    private long favoriteCount;
    private Date latestDate;
    private Date oldestDate;
}
