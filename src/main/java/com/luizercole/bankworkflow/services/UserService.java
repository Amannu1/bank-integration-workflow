package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.RoleDTO;
import com.luizercole.bankworkflow.dto.UserDTO;
import com.luizercole.bankworkflow.dto.UserInsertDTO;
import com.luizercole.bankworkflow.entities.Role;
import com.luizercole.bankworkflow.entities.User;
import com.luizercole.bankworkflow.repositories.RoleRepository;
import com.luizercole.bankworkflow.repositories.UserRepository;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll(){
        List<User> list = userRepository.findAll();

        return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new UserDTO(entity);
    }

    public UserDTO createUser(UserInsertDTO dto){
        User entity = new User();
        copyDtoToEntity(dto, entity);

        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    private void copyDtoToEntity(UserDTO dto, User entity){
        entity.setName(dto.getName());
        entity.getRoles().clear();
        for(RoleDTO roleDto : dto.getRoles()){
            Role role = roleRepository.findById(roleDto.getId()).orElseThrow(() -> new EntityNotFoundException("Role not found."));
            entity.getRoles().add(role);
        }
    }
}
