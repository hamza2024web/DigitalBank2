package dto;

public class LoginRequestDTO {
    private String username;
    private String password;

    public LoginRequestDTO(String username , String password){
        this.username = username;
        this.password = password;
    }

    public String getUername(){
        return username;
    }

    public void setUSername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
