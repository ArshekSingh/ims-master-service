package com.sas.ims.repository;

import com.sas.ims.entity.DistrictMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictMaster, Integer> {

    Optional<DistrictMaster> findByDistrictId(Integer districtId);

    @Query(value = "select DISTRICT_NAME from District_Master dm inner join pincode_master pm on pm.district_Id = dm.district_Id " +
            "where pm.pincode =:pincode and pm.country_id = 1 ", nativeQuery = true)
    String findDistrictNameByPincode(Integer pincode);
}