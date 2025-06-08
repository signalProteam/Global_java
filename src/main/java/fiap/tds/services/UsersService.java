package fiap.tds.services;

import fiap.tds.dtos.UsersDTO;
import fiap.tds.models.Users;
import fiap.tds.repositories.UsersRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UsersService {
    @Inject
    UsersRepository usersRepository;

    public boolean login(String username, String password) {
        var user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get().getPassword().equals(password);
        } else {
            return false;
        }

    }


    public void register (UsersDTO usersDTO) {
        var user = new Users();
        user.setUsername(usersDTO.getUsername());
        user.setPassword(usersDTO.getPassword());
        usersRepository.save(user);
    }

    public Optional<Users> findByUsername(String username) {
        if(!username.isEmpty()){
            return usersRepository.findByUsername(username);
        } else {
            return Optional.empty();
        }
    }



}
