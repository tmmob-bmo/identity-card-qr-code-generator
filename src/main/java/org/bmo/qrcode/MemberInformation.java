package org.bmo.qrcode;

public class MemberInformation {

    private String identityNumber;
    private String registrationNumber;

    public MemberInformation(String identityNumber, String registrationNumber) {
        this.identityNumber = identityNumber;
        this.registrationNumber = registrationNumber;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String toString() {
        return "MemberInformation{" +
                "identityNumber='" + identityNumber + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }
}
