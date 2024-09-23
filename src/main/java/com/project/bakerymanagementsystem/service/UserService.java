package com.project.bakerymanagementsystem.service;

import com.project.bakerymanagementsystem.dto.PhoneNumber;
import com.project.bakerymanagementsystem.data.UserRole;
import com.project.bakerymanagementsystem.entity.Employee;
import com.project.bakerymanagementsystem.repository.UserRepository;
import com.project.bakerymanagementsystem.dto.Address;
import com.project.bakerymanagementsystem.dto.Name;
import com.project.bakerymanagementsystem.dto.UserDTO;
import com.project.bakerymanagementsystem.dto.stat.Stats;
import com.project.bakerymanagementsystem.dto.stat.UserStats;
import com.project.bakerymanagementsystem.exception.NotFoundException;
import com.project.bakerymanagementsystem.exception.UserConflictException;
import com.project.bakerymanagementsystem.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @PersistenceContext
    EntityManager entityManager;

    public void saveUser(Employee user) {
        if (!isExists(user.getEmail())) {
            userRepository.save(user);
        } else throw new UserConflictException("A user is already exists with given email: " + user.getEmail());
    }

    public Employee saveUser(UserDTO userDTO) throws Exception{
        if(isExists(userDTO.getEmail())) {
            throw new UserConflictException("An employee with email  '" + userDTO.getEmail() + "' is already exists.");
        } else {
            Employee user = new Employee();

            user.setPassword(encoder.encode(userDTO.getPassword()));
            user.setJoinDate(new Date());
            user.setName(new Name(userDTO.getFirstName(), userDTO.getLastName()));
            user.getAddressList().add(new Address(userDTO.getAddressLine(), userDTO.getDetails()));
            user.setRole(UserRole.ADMIN); // default value
            user.setEmail(userDTO.getEmail());
            user.getPhoneNumbers().add(new PhoneNumber(userDTO.getPhoneName(),userDTO.getPhoneNumber()));
            user.setSalary(0); // default value

            Employee savedUser = userRepository.save(user);
            String otp = String.format("%06d", (int) (Math.random() * 1000000));
            verificationTokenService.createVerificationToken(savedUser, otp);

            emailService.sendVerificationEmail(savedUser.getEmail(), otp);
            return savedUser;

        }
    }

    public void updateLoginDate(final String email) {
        if(isExists(email)) {
            userRepository.updateLastLogin(new Date(), email);
        }
    }

    public void updateLogoutDate(final String email) {
        if(isExists(email)) {
            userRepository.updateLastLogout(new Date(), email);
        }
    }

    public long countAll() {
        return userRepository.count();
    }

    public UserDTO getUserFromEntity(Employee user) {
        UserDTO userData = new UserDTO();

        userData.setFirstName(user.getName().getFirstName());
        userData.setLastName(user.getName().getLastName());
        userData.setEmail(user.getEmail());
        userData.setRole(user.getRole().toString());

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

        if(user.getLastLoginDate() != null) {
            userData.setLastLoginDate(format.format(user.getLastLoginDate()));
        }

        if(user.getLastLogoutDate() != null) {
            userData.setLastLogoutDate(format.format(user.getLastLogoutDate()));
        }

        return userData;
    }

    public void deleteUser(long id) {
        if(isExists(id)) {
            userRepository.deleteById(id);
        }
        else throw new UserNotFoundException("There is no such employee with given id: '" + id + "'");
    }

    public Employee getUserById(long id) {
        if(isExists(id)) {
            return userRepository.findById(id).get();
        }
        else throw new UserNotFoundException("There is no such employee with given id: '" + id + "'");
    }

    public List<Employee> getAll() {
        return userRepository.findAll();
    }

    public List<Employee> getUsersByRole(UserRole role) {
        List<Employee> users = userRepository.findEmployeesByRole(role);
        return users;
    }

    public int countUsersByRole(UserRole role) {
        return userRepository.countEmployeesByRole(role);
    }

    public Employee getUserByEmail(String email) {
        if(isExists(email)) {
            return userRepository.findEmployeeByEmail(email).get();
        }
        else throw new NotFoundException("There is no employee with email: '" + email + "'");
    }

    public Stats getStats() {
        UserStats userStats = new UserStats();

        userStats.setTotalCount(userRepository.count());
        userStats.setAdminCount(countUsersByRole(UserRole.ADMIN));


        return userStats;
    }

    public void changePassword(long id, String password) {
        if(isExists(id)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            userRepository.updatePassword(id, encoder.encode(password));
        }
        else throw new UserNotFoundException("No such user with given ID.");
    }

    @Transactional
    public void changeAddress(long id, Address address) {
        if(isExists(id)) {
           Employee emp = entityManager.find(Employee.class, id);
           emp.getAddressList().set(0, address);

           entityManager.merge(emp);
        }
        else throw new UserNotFoundException("No such user with given ID.");
    }

    @Transactional
    public void changePhoneNumber(long id, PhoneNumber phoneNumber) throws UserNotFoundException{
        if(isExists(id)) {
            Employee emp = entityManager.find(Employee.class, id);
            emp.getPhoneNumbers().set(0, phoneNumber);

            entityManager.merge(emp);
        }
        else throw new UserNotFoundException("No such user with given ID.");
    }

    public void changeName(long id, Name name) throws UserNotFoundException{
        if(isExists(id)) {
            userRepository.changeName(id, name);
        }
        else throw new UserNotFoundException("No such user with given ID.");
    }

    public void changeEmail(long id, String email) throws UserNotFoundException{
        if(isExists(id)) {
            userRepository.changeEmail(id,email);
        }
        else throw new UserNotFoundException("No such user with given ID.");
    }

    public void assignRole(long id, UserRole role) throws UserNotFoundException{
        if(isExists(id)) {
            if(userRepository.getById(id).getRole() == UserRole.ADMIN) {
                throw new UserConflictException("Admin account can't be updated");
            }
            userRepository.assignRole(id, role);
        }
        else throw new UserNotFoundException("No such user with given ID.");
    }

    public void updateSalary(long id, double salary) throws UserNotFoundException{
        if(isExists(id)) {
            userRepository.updateSalary(id, salary);
        }
        else throw new UserNotFoundException("No such user with given ID.");
    }

    public boolean isExists(long id) {
        if(userRepository.existsById(id)) {
            return true;
        }
        else return false;
    }

    public boolean isExists(String email) {
        if(userRepository.existsByEmail(email)) {
            return true;
        }
        else return false;
    }
}
