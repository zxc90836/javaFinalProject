import java.io.Serializable;

public class Player {
    private String name;
    private int averageDamage;
    private Rank rank;
    //private int id;
    //private Score score;
    Player(){
        this.name = "";
        this.rank = Rank.BRONZE;
        this.averageDamage=0;
        /*this.id=0;
        this.score=new Score();*/
    }
    Player(String name,Rank rank, int averageDamage/*, int id, Score score*/){
        this.name = name;
        if(averageDamage>=0){
            this.averageDamage=averageDamage;
        }
        else
            this.averageDamage=0;
        this.rank = rank;
        /*this.id=id;
        this.score=score;*/
    }

    //getFunction
    public String getName() {
        return name;
    }

    public int getAverageDamage() {
        return this.averageDamage;
    }

    public Rank getRank() {
        return rank;
    }


    /*public int getId() {
        return this.id;
    }

    public Score getScore() {
        return score;
    }*/

    //setFunction
    public void setAverageDamage(int averageDamage) {
        this.averageDamage = averageDamage;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setRank(Rank rank) {
        this.rank = rank;
    }
    /*public void setId(int id) {
        this.id = id;
    }

    public void setScore(Score score) {
        this.score = score;
    }*/

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", averageDamage=" + averageDamage +
                ", rank=" + rank +
                '}';
    }
}
