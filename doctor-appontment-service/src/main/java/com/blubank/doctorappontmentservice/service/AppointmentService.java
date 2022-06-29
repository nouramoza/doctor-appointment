package com.blubank.doctorappontmentservice.service;

import com.blubank.doctorappontmentservice.web.dto.AppointmentDto;
import com.blubank.doctorappontmentservice.web.dto.AppointmentSearchDto;
import com.blubank.doctorappontmentservice.web.dto.GenericRestResponse;
import com.blubank.doctorappontmentservice.web.dto.TakeAppointmentDto;
import org.springframework.stereotype.Service;

@Service
public interface AppointmentService {

    GenericRestResponse saveAppointments(AppointmentDto appointmentDto);

    GenericRestResponse viewAppointmentsAsDoctor(Long doctorId);

    GenericRestResponse deleteOpenAppointment(Long appointmentId);

    GenericRestResponse viewDoctorsOpenAppointments(AppointmentSearchDto appointmentSearchDto);

    GenericRestResponse takeOpenAppointments(TakeAppointmentDto takeAppointmentDto);

}
