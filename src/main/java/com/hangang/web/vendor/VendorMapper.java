package com.hangang.web.vendor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VendorMapper {

    int existsByEmail(String email);

    Vendor findByEmail(String email);

    List<Vendor> findByStatus(String status);

    int insertVendor(Vendor vendor);

    int approve(Long vendorId);

    int reject(Long vendorId);
}
