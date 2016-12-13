package org.jasig.cas.principal;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;

/**
 * Created by stzhang on 2016/12/13.
 */
public class NullPrincipalResolver
        implements PrincipalResolver
{
    public NullPrincipalResolver() {}

    public Principal resolve(Credential credential)
    {
        return null;
    }

    public boolean supports(Credential credential)
    {
        return credential.getId() != null;
    }
}
