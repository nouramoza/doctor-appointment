package com.blubank.doctorappontmentservice.web.dto;
import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class AppointmentDto {
    private Long id;

    @NonNull
    private Long doctorID;

    @NonNull
    private Date startTime;

    @NonNull
    private Date endTime;

    private String patientName;

    private String patientPhoneNumber;

}
