package com.sts.ims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.ims.entity.ProductMaster;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long>{

}
