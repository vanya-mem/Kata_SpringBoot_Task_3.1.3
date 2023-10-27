package ru.kata.spring.boot_security.demo.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userService.loadUserByUsername(user.getUsername());
        }
        catch (UsernameNotFoundException e ) {
            return;
        }
        if (user.getRoles().size() == 0) {
            errors.rejectValue("roles", "", "None roles selected");
        }
        if (user.getPassword().length() < 4 || user.getPassword().equals("")) {
            errors.rejectValue("password", "", "Password must be longer or equal to 4 chars");
        }
        if (user.getUsername().equals("")) {
            errors.rejectValue("username", "", "Username can't be empty");
        }
    }
}
