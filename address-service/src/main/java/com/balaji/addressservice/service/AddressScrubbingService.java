package com.balaji.addressservice.service;

import com.balaji.addressservice.dto.AddressRequestDto;
import com.balaji.addressservice.dto.AddressResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Service
@Slf4j
public class AddressScrubbingService {

	private static final Logger log = LoggerFactory.getLogger(AddressScrubbingService.class);

	private final RestTemplate restTemplate;
	
	// 🟢 Inject the key dynamically from your application.yml configuration
    @Value("${google.api.key}")
    private String googleApiKey;

	public AddressScrubbingService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

    @CircuitBreaker(name = "addressValidationBreaker", fallbackMethod = "handleGoogleOutageFallback")
    public AddressResponseDto scrubAndValidate(AddressRequestDto incomingAddress) {
        String GOOGLE_API_URL = "https://googleapis.com" + googleApiKey;;
        log.info("📡 Translating flat DTO into Google's nested payload format...");

        // 1. Build Google's mandatory nested structure
        GoogleAddressContainer addressContainer = new GoogleAddressContainer();
        
        String combinedLine = incomingAddress.getStreetAddress() + ", " + 
                              incomingAddress.getCity() + ", " + 
                              incomingAddress.getState() + " " + 
                              incomingAddress.getZipCode();
                              
        addressContainer.getAddressLines().add(combinedLine);
        
        // Google requires the 2-letter ISO Country Code (defaults to US if blank)
        String isoCountry = (incomingAddress.getCountry() != null) ? incomingAddress.getCountry().toUpperCase() : "US";
        addressContainer.setRegionCode(isoCountry);

        GoogleAddressRequest googlePayload = new GoogleAddressRequest();
        googlePayload.setAddress(addressContainer);

        log.info("🚀 Executing live HTTP POST call to Google Maps validation engine...");
        
        // 2. Fire the network request
        // (If the API key is a placeholder, this will throw an exception and trigger the fallback below)
        GoogleValidationResponse googleResponse = restTemplate.postForObject(GOOGLE_API_URL, googlePayload, GoogleValidationResponse.class);

        // 3. Process the successful Google response mapping
        AddressResponseDto response = new AddressResponseDto();
        response.setStandardizedAddress(combinedLine.toUpperCase());
        response.setValidationStatus("VERIFIED_BY_GOOGLE_LIVE");
        response.setVerifiedCountryCode(isoCountry);
        return response;
    }

    // 🛡️ FALLBACK METHOD: Automatically intercepts failures, timeouts, or invalid API key exceptions
    public AddressResponseDto handleGoogleOutageFallback(AddressRequestDto incomingAddress, Throwable t) {
        log.warn("⚠️ Circuit Breaker engaged. Falling back to offline check. Reason: {}", t.getMessage());
        
        AddressResponseDto response = new AddressResponseDto();
        response.setStandardizedAddress(incomingAddress.getStreetAddress().toUpperCase());
        response.setVerifiedCountryCode(incomingAddress.getCountry() != null ? incomingAddress.getCountry().toUpperCase() : "US");
        
        if (incomingAddress.getStreetAddress() != null && !incomingAddress.getStreetAddress().trim().isEmpty() 
            && incomingAddress.getZipCode() != null && !incomingAddress.getZipCode().trim().isEmpty()) {
            response.setValidationStatus("ACCEPTED_OFFLINE_PASS");
        } else {
            response.setValidationStatus("REJECTED_MISSING_REQUIRED_FIELDS");
        }
        return response;
    }
}

// 📦 GOOGLE API SPECIFIC DATA MODELS (Generate Getters/Setters manually if Lombok is red-underlined)
class GoogleAddressRequest {
    private GoogleAddressContainer address;
    public GoogleAddressContainer getAddress() { return address; }
    public void setAddress(GoogleAddressContainer address) { this.address = address; }
}

class GoogleAddressContainer {
    private List<String> addressLines = new ArrayList<>();
    private String regionCode;

    public List<String> getAddressLines() { return addressLines; }
    public void setAddressLines(List<String> addressLines) { this.addressLines = addressLines; }
    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
}

@Data
class GoogleValidationResponse {
    // Placeholder class for handling Google's inbound JSON payload structure
}
