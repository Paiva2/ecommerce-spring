package ecommerce.http.enums;

public enum Gender {
    MALE("M"), FEMALE("F"), UNISSEX("U");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }
}
