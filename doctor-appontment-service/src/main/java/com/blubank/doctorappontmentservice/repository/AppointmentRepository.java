package com.blubank.doctorappontmentservice.repository;

import com.blubank.doctorappontmentservice.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findByDoctorID(Long doctorId);

    @Query("select a from AppointmentEntity a where a.doctorID = :doctorId  and a.startTime >= :startDate and a.endTime <= :endDate and a.patientName is null and a.patientPhoneNumber is null order by a.startTime")
    public List<AppointmentEntity> findByDoctorIdAndDate(@Param("doctorId") Long doctorId,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate
    );
}
