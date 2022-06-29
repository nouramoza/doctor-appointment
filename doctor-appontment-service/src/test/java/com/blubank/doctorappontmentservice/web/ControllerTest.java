package com.blubank.doctorappontmentservice.web;

import com.blubank.doctorappontmentservice.util.ConstantsUtil;
import com.blubank.doctorappontmentservice.web.dto.AppointmentDto;
import com.blubank.doctorappontmentservice.web.dto.AppointmentSearchDto;
import com.blubank.doctorappontmentservice.web.dto.TakeAppointmentDto;
import com.blubank.doctorappontmentservice.web.error.ErrorConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static final URI SAVE_TIME_SLOTS_URI = URI.create("/appointment/api/v1/doctor-appointment-service/saveTimeSlots");
    private static final String VIEW_TIME_SLOTS_AS_DPCTOR_URI = "/appointment/api/v1/doctor-appointment-service/viewTimeSlotsAsDoctor/{doctorId}";
    private static final String DELETE_OPEN_APPOINTMENT_AS_DPCTOR_URI = "/appointment/api/v1/doctor-appointment-service/deleteOpenAppointment/{appointmentId}";

    private static final URI VIEW_DOCTORS_OPEN_APPOINTMENT_URI = URI.create("/appointment/api/v1/doctor-appointment-service/viewDoctorsOpenAppointment");
    private static final URI TAKE_OPEN_APPOINTMENT_URI = URI.create("/appointment/api/v1/doctor-appointment-service/takeOpenAppointment");

    @Test
    public void addSlots() throws Exception {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime startDate = DateTime.parse("31/12/2022 10:11:59", formatter);
        DateTime endDate = DateTime.parse("31/12/2022 12:30:59", formatter);

        AppointmentDto appointmentDto = new AppointmentDto(1L, startDate.toDate(), endDate.toDate());
        String inputStr = mapToJson(appointmentDto);
        RequestBuilder req = post(SAVE_TIME_SLOTS_URI)
                .contentType(MediaType.APPLICATION_JSON) // for DTO
                .content(inputStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void findDoctorSlots() throws Exception {
        addSlots();
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get(VIEW_TIME_SLOTS_AS_DPCTOR_URI, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findDoctorSlotsEmpty() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .get(VIEW_TIME_SLOTS_AS_DPCTOR_URI, 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteOpenAppointmentNotFound() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(DELETE_OPEN_APPOINTMENT_AS_DPCTOR_URI, 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andReturn()
                .getResponse()
                .getContentAsString()
                .contains(ErrorConstants.AppointmentMessage.APPOINTMENT_TIME_NOT_FOUND_MSG);
    }

    @Test
    public void deleteOpenAppointment() throws Exception {
        addSlots();
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(DELETE_OPEN_APPOINTMENT_AS_DPCTOR_URI, 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void viewDoctorsOpenAppointment() throws Exception {
        addSlots();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime startDate = DateTime.parse("31/12/2022", formatter);

        AppointmentSearchDto appointmentSearchDto = new AppointmentSearchDto(1L, startDate.toDate());
        String inputStr = mapToJson(appointmentSearchDto);
        RequestBuilder req = get(VIEW_DOCTORS_OPEN_APPOINTMENT_URI)
                .contentType(MediaType.APPLICATION_JSON) // for DTO
                .content(inputStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void takeOpenAppointments() throws Exception {
        addSlots();

        TakeAppointmentDto takeAppointmentDto = new TakeAppointmentDto(2L, "Noura", "09113149630");
        String inputStr = mapToJson(takeAppointmentDto);
        RequestBuilder req = post(TAKE_OPEN_APPOINTMENT_URI)
                .contentType(MediaType.APPLICATION_JSON) // for DTO
                .content(inputStr);

        MvcResult mvcResult = this.mockMvc.perform(req)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    protected static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
