package com.sas.ims.repository;

import com.sas.ims.entity.PincodeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PincodeRepository extends JpaRepository<PincodeMaster, Integer> {

    @Query(value = "SELECT * FROM pincode_master WHERE CAST(pincode AS TEXT) LIKE CONCAT('%',:pincode,'%') and ACTIVE = 'Y'", nativeQuery = true)
    List<PincodeMaster> findAllByPincodeMaster(String pincode);

    Optional<PincodeMaster> findByPincodeMasterPK_PincodeAndPincodeMasterPK_CountryId(Integer pincode, Integer countryId);

    Optional<PincodeMaster> findByPincodeMasterPK_Pincode(Integer pincode);
}