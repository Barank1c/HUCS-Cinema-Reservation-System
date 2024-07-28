public class User {
    private String username,hashedPassword,clubMember,admin;

    public User(String username, String hashedPassword, String clubMember, String admin) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.clubMember = clubMember;
        this.admin = admin;
    }

    public void setClubMember(String clubMember) {
        this.clubMember = clubMember;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getClubMember() {
        return clubMember;
    }

    public String getAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return username;
    }
}
