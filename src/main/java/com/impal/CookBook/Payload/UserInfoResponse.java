package com.impal.CookBook.Payload;

public class UserInfoResponse {
    
    private String imdbId;
    private String username;
    private String profilePic;

    public UserInfoResponse(String imdbId, String username, String profilePic) {
        this.imdbId = imdbId;
        this.username = username;
        this.profilePic = profilePic;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    
}
