package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.banking.academy.entity.User;

public interface UserRepository extends JpaRepository<User,String> {


}
