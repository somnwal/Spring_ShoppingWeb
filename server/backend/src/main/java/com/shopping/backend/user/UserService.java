package com.shopping.backend.user;

import com.shopping.backend.user.exception.UserNotFoundException;
import com.shopping.entity.Role;
import com.shopping.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

   public static final int PAGE_SIZE = 4;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public Page<User> getUsersByPaging(int pageNum, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

        System.out.println(keyword);

        if (keyword != null) {
            return userRepository.findAll(keyword, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    public User getUser(Long id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch(Exception e) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다 : " + id);
        }
    }

    public List<Role> getRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {

        if (user.getId() != null) {
            User existingUser = userRepository.findById(user.getId()).get();

            if(user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isEmailExists(Long id, String email) {
        User user = userRepository.findUserByEmail(email);

        if(user == null) {
            return false;
        }

        // 신규 추가 시
        if(id == null) {
            // 이미 사용자가 있으면 False
            return true;
        } else {
            // 수정 중인 사용자면 ID 일치여부 검증
            return !user.getId().equals(id);
        }
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
