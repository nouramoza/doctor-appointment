package com.blubank.doctorappontmentservice.service.impl;

import com.blubank.doctorappontmentservice.entity.AppointmentEntity;
import com.blubank.doctorappontmentservice.repository.AppointmentRepository;
import com.blubank.doctorappontmentservice.service.AppointmentService;
import com.blubank.doctorappontmentservice.util.ConstantsUtil;
import com.blubank.doctorappontmentservice.web.dto.AppointmentDto;
import com.blubank.doctorappontmentservice.web.dto.AppointmentSearchDto;
import com.blubank.doctorappontmentservice.web.dto.GenericRestResponse;
import com.blubank.doctorappontmentservice.web.dto.TakeAppointmentDto;
import com.blubank.doctorappontmentservice.web.error.BadRequestAlertException;
import com.blubank.doctorappontmentservice.web.error.ErrorConstants;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for Doctor Making TimeSlots.
 */

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final String APPOINTMENT_DTO = "appointmentDto";

    private AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }


    /**
     * Generate Doctor's available TimeSlots
     *
     * @param appointmentDto Doctor Start and end data and time
     * @return GenericRestResponse makes time slots and save
     */
    @Override
    public GenericRestResponse saveAppointments(AppointmentDto appointmentDto) {
        GenericRestResponse response;
        try {
            makeAppointmentValidation(appointmentDto);
            List<AppointmentEntity> appointmentEntityList = makeTimeSlots(appointmentDto);
            response = saveSlots(appointmentEntityList);
        } catch (BadRequestAlertException e) {
            response = new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                    e.getMessage() != null ? e.getMessage() : e.getStackTrace().toString());
        }
        return response;
    }

    @Override
    public GenericRestResponse viewAppointmentsAsDoctor(Long doctorId) {
        GenericRestResponse response = null;
        try {
            List<AppointmentEntity> appointmentEntityList = appointmentRepository.findByDoctorID(doctorId);
            response = new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
                    "Doctor " + doctorId + " Appointment Timings",
                    appointmentEntityList);
        } catch (Exception e) {
            response = new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                    e.getMessage() != null ? e.getMessage() : e.getStackTrace().toString());
        }
        return response;
    }

    @Override
    public GenericRestResponse deleteOpenAppointment(Long appointmentId) {
        GenericRestResponse response = null;
        try {
            Optional<AppointmentEntity> timeSlotEntity = appointmentRepository.findById(appointmentId);
            if (timeSlotEntity.isPresent()) {
                if (timeSlotEntity.get().getPatientName() == null && timeSlotEntity.get().getPatientPhoneNumber() == null) {
                    appointmentRepository.delete(timeSlotEntity.get());
                    response = new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
                            "The OpenAppointment Number " + appointmentId + " Is Deleted Successfully");
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE, ErrorConstants.AppointmentMessage.APPOINTMENT_TIME_NOT_OPEN_MSG);
                }
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ErrorConstants.AppointmentMessage.APPOINTMENT_TIME_NOT_FOUND_MSG);
            }
        } catch (Exception e) {
            response = new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                    e.getMessage() != null ? e.getMessage() : e.getStackTrace().toString());
        }
        return response;
    }

    @Override
    public GenericRestResponse viewDoctorsOpenAppointments(AppointmentSearchDto appointmentSearchDto) {
        GenericRestResponse response = null;
        try {
            Date startDate = appointmentSearchDto.getDate();
            Date endDate = DateUtils.addHours(appointmentSearchDto.getDate(), 24);
            List<AppointmentEntity> appointmentEntityList = appointmentRepository.findByDoctorIdAndDate(
                    appointmentSearchDto.getDoctorId(),
                    startDate, endDate
            );
            response = new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
                    "Doctor " + appointmentSearchDto.getDoctorId() + " Appointment Timings",
                    appointmentEntityList);

        } catch (Exception e) {
            response = new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                    e.getMessage() != null ? e.getMessage() : e.getStackTrace().toString());
        }
        return response;
    }

    @Override
    public GenericRestResponse takeOpenAppointments(TakeAppointmentDto takeAppointmentDto) {
        GenericRestResponse response = null;
        try {
            takeAppointmentValidation(takeAppointmentDto);

            Optional<AppointmentEntity> appointmentEntityOptional = appointmentRepository.findById(takeAppointmentDto.getAppointmentId());

            if (!appointmentEntityOptional.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ErrorConstants.AppointmentMessage.APPOINTMENT_TIME_NOT_FOUND_MSG);
            }

            if (appointmentEntityOptional.get().getPatientName() == null && appointmentEntityOptional.get().getPatientPhoneNumber() == null) {
                appointmentEntityOptional.get().setPatientName(takeAppointmentDto.getPatientName());
                appointmentEntityOptional.get().setPatientPhoneNumber(takeAppointmentDto.getPatientPhoneNumber());
                appointmentRepository.saveAndFlush(appointmentEntityOptional.get());
                response = new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS,
                        "The OpenAppointment Number " + takeAppointmentDto.getAppointmentId() + " Is Taken Successfully");
            } else {
                throw new BadRequestAlertException(ErrorConstants.AppointmentMessage.APPOINTMENT_TIME_NOT_OPEN_MSG, APPOINTMENT_DTO,
                        ErrorConstants.AppointmentMessage.APPOINTMENT_TIME_NOT_OPEN_KEY);
            }
        } catch (Exception e) {
            response = new GenericRestResponse(GenericRestResponse.STATUS.FAILURE,
                    e.getMessage() != null ? e.getMessage() : e.getStackTrace().toString());
        }
        return response;
    }

    /**
     * Validate Entry values
     *
     * @param appointmentDto Doctor Start and end data and time
     */
    private void makeAppointmentValidation(AppointmentDto appointmentDto) throws BadRequestAlertException {
        if (appointmentDto.getStartTime().after(appointmentDto.getEndTime())) {
            throw new BadRequestAlertException(ErrorConstants.DateTimeValidationMessage.START_END_TIME_NOT_VALID_MSG, APPOINTMENT_DTO, ErrorConstants.DateTimeValidationMessage.START_END_TIME_NOT_VALID_KEY);
        }
    }

    /**
     * Makes 30 Minutes time slots that are open to be taken
     *
     * @param appointmentDto Doctor Start and end data and time
     * @return List<TimeSlotEntity> list of available times of the doctor for the given date and time
     */
    private List<AppointmentEntity> makeTimeSlots(AppointmentDto appointmentDto) {
        List<AppointmentEntity> appointmentEntityList = new ArrayList<>();
        Date startTime = appointmentDto.getStartTime(),
                endTime = appointmentDto.getEndTime();
        while (startTime.before(endTime)) {
            AppointmentEntity appointmentEntity = new AppointmentEntity(1L, startTime,
                    DateUtils.addMinutes(startTime, ConstantsUtil.CommonValue.APPOINTMENT_DURATION));
            if (!appointmentEntity.getEndTime().after(endTime)) {
                appointmentEntityList.add(appointmentEntity);
            }
            startTime = appointmentEntity.getEndTime();
        }
        return appointmentEntityList;
    }

    /**
     * saves doctor's TimeSlots
     *
     * @param appointmentEntityList List of doctor's available times of given day
     * @return GenericRestResponse success or failure of service
     */
    private GenericRestResponse saveSlots(List<AppointmentEntity> appointmentEntityList) {
        appointmentEntityList.stream()
                .forEach(ts -> {
                    appointmentRepository.saveAndFlush(ts);
                });
        return new GenericRestResponse(GenericRestResponse.STATUS.SUCCESS, appointmentEntityList.size() + ConstantsUtil.ResponseMessage.SLOTS_ADDED_SUCCESSFULLY);
    }

    private void takeAppointmentValidation(TakeAppointmentDto takeAppointmentDto) throws BadRequestAlertException {
        if (takeAppointmentDto.getPatientName() == null) {
            throw new BadRequestAlertException(ErrorConstants.AppointmentMessage.NAME_IS_EMPTY_MSG, APPOINTMENT_DTO,
                    ErrorConstants.AppointmentMessage.NAME_IS_EMPTY_KEY);
        }
        if (takeAppointmentDto.getPatientPhoneNumber() == null) {
            throw new BadRequestAlertException(ErrorConstants.AppointmentMessage.PHONE_NUMBER_IS_EMPTY_MSG, APPOINTMENT_DTO,
                    ErrorConstants.AppointmentMessage.PHONE_NUMBER_IS_EMPTY_KEY);
        }

    }

}
