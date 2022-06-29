package com.example.daba.Repository;

import com.example.daba.Models.NationalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface NationalIdRepository extends JpaRepository<NationalId, String> {

    NationalId findByPersonalCode(String personalCode);

    List<NationalId> findAllByBirthDateBetween(Date from, Date to);

    @Modifying
    @Transactional
    @Query(value = "delete from national_id where gender is null", nativeQuery = true)
    void deleteNationalId();
}
