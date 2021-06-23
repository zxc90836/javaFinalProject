public class RankTypeHandler {
    public Rank produceRank(String rank){
        Rank[] type = Rank.values();
        if(rank.compareTo(Rank.BRONZE.getName())==0){
            return type[0];
        }
        else if(rank.compareTo(Rank.SILVER.getName())==0){
            return type[1];
        }
        else if(rank.compareTo(Rank.GOLD.getName())==0){
            return type[2];
        }
        else if(rank.compareTo(Rank.PLATINUM.getName())==0){
            return type[3];
        }
        else if(rank.compareTo(Rank.DIAMOND.getName())==0){
            return type[4];
        }
        else if(rank.compareTo(Rank.MASTER.getName())==0){
            return type[5];
        }
        else if(rank.compareTo(Rank.PREDATOR.getName())==0){
            return type[6];
        }
        return type[0];
    }
}
