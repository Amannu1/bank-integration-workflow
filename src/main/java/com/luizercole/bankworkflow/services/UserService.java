package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.UserDTO;
import com.luizercole.bankworkflow.entities.User;
import com.luizercole.bankworkflow.repositories.RoleRepository;
import com.luizercole.bankworkflow.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll(){
        List<User> list = userRepository.findAll();

        return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
    }
}
