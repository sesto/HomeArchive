package be.ordina.sest.homearchive.security.transfer;

import java.util.Map;

/**
 * Created by sest on 25/11/14.
 */
public class UserTransfer {

        private final String name;

        private final Map<String, Boolean> roles;


        public UserTransfer(String userName, Map<String, Boolean> roles)
        {
            this.name = userName;
            this.roles = roles;
        }


    public String getName()
    {
        return this.name;
    }


    public Map<String, Boolean> getRoles()
    {
        return this.roles;
    }

}
