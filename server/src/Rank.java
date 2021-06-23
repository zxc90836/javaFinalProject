import java.io.Serializable;

public enum Rank {
    BRONZE("青銅"),SILVER("白銀"),GOLD("黃金"),PLATINUM("白金")
    ,DIAMOND("鑽石"),MASTER("大師"),PREDATOR("頂獵");
    private final String name;
    private Rank(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Rank{" +
                "牌位='" + name + '\'' +
                '}';
    }
}

