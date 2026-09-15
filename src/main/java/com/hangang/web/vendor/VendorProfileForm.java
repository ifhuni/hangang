package com.hangang.web.vendor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VendorProfileForm {

    @NotBlank(message = "상호명을 입력해주세요.")
    private String businessName;

    @NotBlank(message = "사업자등록번호를 입력해주세요.")
    private String businessRegNo;

    @NotBlank(message = "대표자명을 입력해주세요.")
    private String ceoName;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String phone;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String newPassword;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessRegNo() {
        return businessRegNo;
    }

    public void setBusinessRegNo(String businessRegNo) {
        this.businessRegNo = businessRegNo;
    }

    public String getCeoName() {
        return ceoName;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
