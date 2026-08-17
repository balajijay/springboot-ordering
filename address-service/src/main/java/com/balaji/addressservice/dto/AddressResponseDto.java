package com.balaji.addressservice.dto;

public class AddressResponseDto {
    private String standardizedAddress;
    private String validationStatus; // VERIFIED_BY_GOOGLE, ACCEPTED_OFFLINE_PASS, REJECTED
    private String verifiedCountryCode;
    
 // Standard plain Java Getters and Setters
    public String getStandardizedAddress() { return standardizedAddress; }
    public void setStandardizedAddress(String standardizedAddress) { this.standardizedAddress = standardizedAddress; }
    public String getValidationStatus() { return validationStatus; }
    public void setValidationStatus(String validationStatus) { this.validationStatus = validationStatus; }
    public String getVerifiedCountryCode() { return this.verifiedCountryCode; }
    public void setVerifiedCountryCode(String verifiedCountryCode ) { this.verifiedCountryCode = verifiedCountryCode; }
}
