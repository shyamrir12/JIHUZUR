package com.example.awizom.jihuzur.Model;

import java.util.List;

public class UserLogin {


    public class RootObject {



        public String CreatedDate;
        public boolean ActiveStatus;
        public boolean BusyStatus;
        public String Id;
        public String Message;
        public String Mobile;
        public String  Otp;
        public String Role;
        public boolean Status;


        public String  OtpCode;
        public DataProfile dataProfile;
        public String IdentityImage;



        public DataProfile getDataProfile() {
            return dataProfile;
        }


        public void setDataProfile(DataProfile dataProfile) {
            this.dataProfile = dataProfile;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        public String getOtpCode() {
            return OtpCode;
        }

        public void setOtpCode(String otpCode) {
            OtpCode = otpCode;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getRole() {
            return Role;
        }

        public void setRole(String role) {
            Role = role;
        }

        public String getIdentityImage() {
            return IdentityImage;
        }

        public void setIdentityImage(String identityImage) {
            IdentityImage = identityImage;
        }

        public boolean isBusyStatus() {
            return BusyStatus;
        }

        public void setBusyStatus(boolean busyStatus) {
            BusyStatus = busyStatus;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

        public boolean isActiveStatus() {
            return ActiveStatus;
        }

        public void setActiveStatus(boolean activeStatus) {
            ActiveStatus = activeStatus;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getOtp() {
            return Otp;
        }

        public void setOtp(String otp) {
            Otp = otp;
        }
    }

}