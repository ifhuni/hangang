package com.hangang.web.vendor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VendorMapper {

    int existsByEmail(String email);

    int existsByEmailExcludingVendor(@Param("email") String email, @Param("vendorId") Long vendorId);

    Vendor findByEmail(String email);

    Vendor findById(Long vendorId);

    List<Vendor> findByStatus(String status);

    List<Vendor> findAll();

    int insertVendor(Vendor vendor);

    int approve(Long vendorId);

    int reject(Long vendorId);

    int updateProfile(Vendor vendor);

    int updatePassword(@Param("vendorId") Long vendorId, @Param("password") String password);
}
