package com.blubank.doctorappontmentservice.web.error;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.blu.com/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI ENTITY_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/entity-not-found");

    private ErrorConstants() {
    }

    public static class DateTimeValidationMessage {
        public static final String START_END_TIME_NOT_VALID_KEY = "startEndTimeNotValid";
        public static final String START_END_TIME_NOT_VALID_MSG = "End Time is Before Start Time.";
    }

    public static class AppointmentMessage {
        public static final String APPOINTMENT_TIME_NOT_OPEN_KEY = "appointmentNotOpen";
        public static final String APPOINTMENT_TIME_NOT_OPEN_MSG = "The Appointment You have Selected is Not Open";
        public static final String APPOINTMENT_TIME_NOT_FOUND_KEY = "appointmentNotFound";
        public static final String APPOINTMENT_TIME_NOT_FOUND_MSG = "The Appointment You have Entered is Not Found.";

        public static final String NAME_IS_EMPTY_KEY = "nameIsEmpty";
        public static final String NAME_IS_EMPTY_MSG = "Name Of Patient Should Not be Empty";
        public static final String PHONE_NUMBER_IS_EMPTY_KEY = "phoneNumberIsEmpty";
        public static final String PHONE_NUMBER_IS_EMPTY_MSG = "Phone Number Of Patient Should Not be Empty";
    }

}