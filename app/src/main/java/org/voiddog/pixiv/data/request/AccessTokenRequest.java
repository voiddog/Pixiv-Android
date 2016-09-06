package org.voiddog.pixiv.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 获取 Oauth2 access token
 * Created by qgx44 on 2016/9/1.
 */
public class AccessTokenRequest {
    @Expose(serialize = false, deserialize = false)
    public static final String PASSWORD_TYPE = "password";
    @Expose(serialize = false, deserialize = false)
    public static final String REFRESH_TOKEN_TYPE = "refresh_token";

    @SerializedName("client_id")
    public final String clientId = "BVO2E8vAAikgUBW8FYpi6amXOjQj";

    @SerializedName("client_secret")
    public final String clientSecret = "LI1WsFUDrrquaINOdarrJclCrkTtc3eojCOswlog";

    @SerializedName("get_secure_url")
    public final boolean getSecurIUrl = true;

    @SerializedName("grant_type")
    public String grantType;

    @SerializedName("device_token")
    public String deviceToken = "562e3aeb783cd42887c7a6c4495cc79f";

    public String username;

    public String password;

    @SerializedName("refresh_token")
    public String refreshToken;
}
