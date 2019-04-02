package com.example.awizom.jihuzur.Model;

import java.util.List;

public class UserLogin {


    public class RootObject {
        public String Message;
        public boolean Status;
        public String  OtpCode;
        public DataProfile dataProfile;

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

        public DataProfile getDataProfile() {
            return dataProfile;
        }


        public void setDataProfile(DataProfile dataProfile) {
            this.dataProfile = dataProfile;
        }


    }

}