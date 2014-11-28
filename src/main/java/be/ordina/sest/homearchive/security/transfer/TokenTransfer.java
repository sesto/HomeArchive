package be.ordina.sest.homearchive.security.transfer;

/**
 * Token transfer object
 * Created by sest on 25/11/14.
 */
public class TokenTransfer {
    private final String token;


    public TokenTransfer(String token)
    {
        this.token = token;
    }


    public String getToken()
    {
        return this.token;
    }

}
