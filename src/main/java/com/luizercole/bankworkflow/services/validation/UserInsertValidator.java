package com.luizercole.bankworkflow.services.validation;

import com.luizercole.bankworkflow.dto.UserInsertDTO;
import com.luizercole.bankworkflow.entities.User;
import com.luizercole.bankworkflow.repositories.UserRepository;
import com.luizercole.bankworkflow.resources.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserInsertDTO userInsertDTO, ConstraintValidatorContext constraintValidatorContext) {
        List<FieldMessage> fieldMessageList = new ArrayList<>();

        User user = userRepository.findByName(userInsertDTO.getName());

        if(user != null){
            fieldMessageList.add(new FieldMessage("name", "Name already exists"));
        }

        for (FieldMessage e : fieldMessageList) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return fieldMessageList.isEmpty();
    }
}
