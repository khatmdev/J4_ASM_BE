package org.example.java4_asm_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedFriendDTO {
    private String senderName;
    private String senderEmail;
    private String receiverEmail;
    private Date sentDate;
}
