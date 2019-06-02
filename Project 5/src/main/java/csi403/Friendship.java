package csi403;

public class Friendship {

    String[] friends;

    public Friendship() {
        // Empty
    }

    public Friendship(String[] friends) {
        this.friends = friends;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return " [ " + friends[0] + " , " + friends[1] + " ] ";
    }
}
