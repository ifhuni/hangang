package com.hangang.web.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hangang.web.vendor.Vendor;
import com.hangang.web.vendor.VendorMapper;

@Service
public class AdminService {

    private final VendorMapper vendorMapper;

    public AdminService(VendorMapper vendorMapper) {
        this.vendorMapper = vendorMapper;
    }

    public List<Vendor> listPendingVendors() {
        return vendorMapper.findByStatus("PENDING");
    }

    public List<Vendor> listAllVendors() {
        return vendorMapper.findAll();
    }

    public void approveVendor(Long vendorId) {
        vendorMapper.approve(vendorId);
    }

    public void rejectVendor(Long vendorId) {
        vendorMapper.reject(vendorId);
    }
}
