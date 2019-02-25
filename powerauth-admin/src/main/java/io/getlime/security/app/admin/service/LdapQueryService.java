/*
 * Copyright 2019 Wultra s.r.o.
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
package io.getlime.security.app.admin.service;

import io.getlime.security.app.admin.ApplicationConfiguration;
import io.getlime.security.app.admin.security.SecurityMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service for querying LDAP authentication details.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
@Service
public class LdapQueryService {

    private final ApplicationConfiguration configuration;

    /**
     * LDAP query service constructor.
     * @param configuration Application configuration.
     */
    @Autowired
    public LdapQueryService(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Get LDAP username if user is authenticated or null otherwise.
     * @return LDAP username.
     */
    public String getLdapUsername() {
        if (!SecurityMethod.isLdap(configuration.getSecurityMethod())) {
            return null;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        }
        return null;
    }
}
