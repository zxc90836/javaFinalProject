import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class chatRoom {
    private String name;
    private int key;
    private double damageRange;
    private String rank;
    private List<Player> player = new ArrayList<Player>();
    private int memberNum = 0;
    public chatRoom(){
        key = -1;
        name = "Play together!!!";
        damageRange = -1;
        rank = "銅牌";
    }

    public String getName() {
        return name;
    }//getter
    public int getKey() {
        return key;
    }
    public double getDamageRange() {
        return damageRange;
    }
    public String getRank() {
        return rank;
    }
    public List<Player> getPlayer() {
        return player;
    }
    public int getMemberNum() {
        return memberNum;
    }

    public void setName(String name) {
        this.name = name;
    }//setter
    public void setKey(int key) {
        this.key = key;
    }
    public void setDamageRange(double damageRange) {
        this.damageRange = damageRange;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public void setPlayer(List<Player> player) {
        this.player = player;
    }
    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public chatRoom(int key){
        this.key = key;
    }
    public chatRoom(String name,double damageRange,String rank)
    {
        this.name = name;
        this.damageRange = damageRange;
        this.rank = rank;
    }
    public chatRoom(chatRoom c,int key){
        name = c.name;
        damageRange = c.damageRange;
        rank = c.rank;
        this.player = new ArrayList<>(c.player);
        this.memberNum = c.memberNum;
        this.key = key;
    }
    public chatRoom(chatRoom c){
        name = c.name;
        damageRange = c.damageRange;
        rank = c.rank;
        this.key = c.key;
        this.player = new ArrayList<>(c.player);
        this.memberNum = c.memberNum;
    }
    public void addMember(Player user){
        if(memberNum < 3){
            player.add(user);
            memberNum++;
        }
        else
            System.out.println("聊天室已滿!");
    }
    public void signOut(Player delPlayer){
        player.remove(delPlayer);
        memberNum--;
    }
    @Override
    public String toString(){
        return "房名：" + this.name + "    key：" + this.key + "    dmg：" + this.damageRange + "    rank：" + this.rank;
    }
}
