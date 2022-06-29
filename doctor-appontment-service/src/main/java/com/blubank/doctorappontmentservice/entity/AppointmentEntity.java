package com.blubank.doctorappontmentservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "APPOINTMENT")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DOCTOR_ID", nullable=false)
    @NonNull
    private Long doctorID;

    @Column(name = "START_TIME", nullable=false)
    @NonNull
    private Date startTime;

    @Column(name = "END_TIME", nullable=false)
    @NonNull
    private Date endTime;

    @Column(name = "PATIENT_NAME", nullable=true)
    private String patientName;

    @Column(name = "PATIENT_PHONE_NUMBER", nullable=true)
    private String patientPhoneNumber;
}
