package Plants;

import java.util.ArrayList;
import java.util.Arrays;

public class Mais extends Plant {
    public Mais() {
        this.name = "Maïs";
        this.buyMoney = 30;
        this.sellMoney = 50;
        this.durations = new ArrayList<>(Arrays.asList(2, 4));
        this.emojiList = new ArrayList<>(Arrays.asList("\uD83E\uDED8", "\uD83C\uDF31", "\uD83C\uDF3D"));
    }
}