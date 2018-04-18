/*
 * Copyright 2017 Lime - HighTech Solutions s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getlime.security.app.admin.controller;

import io.getlime.security.app.admin.ApplicationConfiguration;
import io.getlime.security.app.admin.model.ServiceStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing controller used for service and maintenance purpose.
 *
 * @author Roman Strobl, roman.strobl@lime-company.eu
 */
@Controller
@RequestMapping(value = "/api/service")
public class ServiceController {

    private final ApplicationConfiguration applicationConfiguration;
    private final BuildProperties buildProperties;

    /**
     * Service constructor.
     * @param applicationConfiguration PowerAuth admin configuration.
     * @param buildProperties Build info.
     */
    @Autowired
    public ServiceController(ApplicationConfiguration applicationConfiguration, BuildProperties buildProperties) {
        this.applicationConfiguration = applicationConfiguration;
        this.buildProperties = buildProperties;
    }

    /**
     * Controller resource with system information.
     * @return System status info.
     */
    @RequestMapping(value = "status", method = RequestMethod.GET)
    public @ResponseBody ServiceStatusResponse getServiceStatus() throws Exception {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Received getServiceStatus request");
        ServiceStatusResponse response = new ServiceStatusResponse();
        response.setApplicationName(applicationConfiguration.getApplicationName());
        response.setApplicationDisplayName(applicationConfiguration.getApplicationDisplayName());
        response.setApplicationEnvironment(applicationConfiguration.getApplicationEnvironment());
        response.setVersion(buildProperties.getVersion());
        response.setBuildTime(buildProperties.getTime());
        response.setTimestamp(new Date());
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "The getServiceStatus request succeeded");
        return response;
    }
}