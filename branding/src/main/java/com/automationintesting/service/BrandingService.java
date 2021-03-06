package com.automationintesting.service;

import com.automationintesting.db.BrandingDB;
import com.automationintesting.model.db.Branding;
import com.automationintesting.model.service.BrandingResult;
import com.automationintesting.requests.AuthRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Service
public class BrandingService {

    private BrandingDB brandingDB;
    private AuthRequests authRequest;

    public BrandingService() throws SQLException {
        DatabaseScheduler databaseScheduler = new DatabaseScheduler();
        databaseScheduler.startScheduler(brandingDB, TimeUnit.MINUTES);

        brandingDB = new BrandingDB();

        authRequest = new AuthRequests();
    }

    public Branding getBrandingDetails() throws SQLException {
        return brandingDB.queryBranding();
    }

    public BrandingResult updateBrandingDetails(Branding branding, String token) throws SQLException {
        if(authRequest.postCheckAuth(token)){
            Branding updatedBranding = brandingDB.update(branding);

            return new BrandingResult(HttpStatus.ACCEPTED, updatedBranding);
        } else {
            return new BrandingResult(HttpStatus.FORBIDDEN);
        }
    }
}
