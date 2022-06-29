package com.blubank.doctorappontmentservice.web.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchDto {

    private Long doctorId;
    private Date date;
}
