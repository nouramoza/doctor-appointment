package com.blubank.doctorappontmentservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeAppointmentDto {

    @NonNull
    private Long appointmentId;

    @NonNull
    private String patientName;

    @NonNull
    private String patientPhoneNumber;
}
