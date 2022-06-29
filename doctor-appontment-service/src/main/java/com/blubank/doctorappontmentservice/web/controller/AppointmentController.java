package com.blubank.doctorappontmentservice.web.controller;

import com.blubank.doctorappontmentservice.service.AppointmentService;
import com.blubank.doctorappontmentservice.web.dto.AppointmentDto;
import com.blubank.doctorappontmentservice.web.dto.AppointmentSearchDto;
import com.blubank.doctorappontmentservice.web.dto.GenericRestResponse;
import com.blubank.doctorappontmentservice.web.dto.TakeAppointmentDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointment/api/v1/doctor-appointment-service")
public class AppointmentController {
    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @RequestMapping(value = "/saveTimeSlots", method = RequestMethod.POST)
    public GenericRestResponse saveTimeSlots(
            @RequestBody AppointmentDto appointmentDto) {
        return appointmentService.saveAppointments(appointmentDto);

    }

    @RequestMapping(value = "/viewTimeSlotsAsDoctor/{doctorId}", method = RequestMethod.GET)
    public GenericRestResponse viewTimeSlotsAsDoctor(
            @PathVariable(value="doctorId") Long doctorId) {
        return appointmentService.viewAppointmentsAsDoctor(doctorId);

    }

    @RequestMapping(value = "/deleteOpenAppointment/{appointmentId}", method = RequestMethod.DELETE)
    public GenericRestResponse deleteOpenAppointment(
            @PathVariable(value="appointmentId") Long appointmentId) {
        return appointmentService.deleteOpenAppointment(appointmentId);
    }

    @RequestMapping(value = "/viewDoctorsOpenAppointment", method = RequestMethod.GET)
    public GenericRestResponse viewDoctorsOpenAppointment(@RequestBody AppointmentSearchDto appointmentSearchDto) {
        return appointmentService.viewDoctorsOpenAppointments(appointmentSearchDto);
    }

    @RequestMapping(value = "/takeOpenAppointment", method = RequestMethod.POST)
    public GenericRestResponse takeOpenAppointment(@RequestBody TakeAppointmentDto takeAppointmentDto) {
        return appointmentService.takeOpenAppointments(takeAppointmentDto);
    }

}
