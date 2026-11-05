package com.pedsouto.userservice.infra.repository;

import com.pedsouto.userservice.infra.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByIdAndUserId(Long id, Long userId);
}
