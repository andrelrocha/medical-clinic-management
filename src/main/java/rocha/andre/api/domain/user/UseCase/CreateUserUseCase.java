package rocha.andre.api.domain.user.UseCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import rocha.andre.api.domain.ValidationException;
import rocha.andre.api.domain.user.*;

@Component
public class CreateUserUseCase {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserReturnDto createUser(UserDto data) {
        boolean userExists = userRepository.userExistsByLogin(data.login());

        if (userExists) {
            throw new ValidationException("Email on user creation already exists in our database");
        }

        User newUser = new User(data);

        String encodedPassword = bCryptPasswordEncoder.encode(data.password());
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);

        return new UserReturnDto(newUser);
    }
}