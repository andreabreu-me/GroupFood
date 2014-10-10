package com.dwbook.phonebook;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class PhonebookAuthenticator implements Authenticator<BasicCredentials, Boolean> {
    public Optional<Boolean> authenticate(BasicCredentials c) throws AuthenticationException {
        if (c.getUsername().equals("john_doe") && c.getPassword().equals("secret")) {
            return Optional.of(true);
        }
        return Optional.absent();
    }
}
