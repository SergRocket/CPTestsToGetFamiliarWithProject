package Utils.Enums;

public enum  SocialMedia {
    LINKEDIN("Linkedin");

    private String typeName;

    SocialMedia(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String toStr(){return typeName;}
}
