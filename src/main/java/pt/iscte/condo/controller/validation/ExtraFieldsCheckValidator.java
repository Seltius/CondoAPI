package pt.iscte.condo.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pt.iscte.condo.controller.dto.request.CreateMeetingRequest;

public class ExtraFieldsCheckValidator implements ConstraintValidator<ExtraFieldsCheck, CreateMeetingRequest> {
    @Override
    public boolean isValid(CreateMeetingRequest request, ConstraintValidatorContext context) {
        if (request.getExtraDate() != null && (request.getExtraStartTime() == null || request.getExtraEndTime() == null)) {
            context.disableDefaultConstraintViolation();
            if (request.getExtraStartTime() == null) {
                context.buildConstraintViolationWithTemplate("extraStartTime cannot be null if extraDate is not null")
                        .addPropertyNode("extraStartTime")
                        .addConstraintViolation();
            }
            if (request.getExtraEndTime() == null) {
                context.buildConstraintViolationWithTemplate("extraEndTime cannot be null if extraDate is not null")
                        .addPropertyNode("extraEndTime")
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
