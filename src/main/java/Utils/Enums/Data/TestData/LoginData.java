package Utils.Enums.Data.TestData;

import Utils.Enums.ObjectUtils;

public abstract class LoginData {
    protected String email;
    protected String password;

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public String toString(){return ObjectUtils.createToString(email, password);
    }
}
