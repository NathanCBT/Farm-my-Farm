package Plants;

import java.util.ArrayList;
import java.util.Arrays;

public class Patate extends Plant {
    public Patate() {
        this.name = "Patate";
        this.buyMoney = 10;
        this.sellMoney = 20;
        this.durations = new ArrayList<>(Arrays.asList(1, 2));
        this.emojiList = new ArrayList<>(Arrays.asList("\uD83E\uDED8", "\uD83C\uDF31", "\uD83E\uDD54"));
    }
}