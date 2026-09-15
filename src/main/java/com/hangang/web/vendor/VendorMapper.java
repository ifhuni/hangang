package com.hangang.web.vendor;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VendorMapper {

    int existsByEmail(String email);

    Vendor findByEmail(String email);

    int insertVendor(Vendor vendor);
}
