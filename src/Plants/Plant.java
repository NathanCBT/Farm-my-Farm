package Plants;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.ArrayList;

public abstract class Plant {
    public String name;
    public double buyMoney;
    public double sellMoney;
    public ArrayList<Integer> durations;
    public ArrayList<String> emojiList;
    public boolean collectAuthorized = false;

    public void growthDuration(Button land) {
            if (!collectAuthorized) {
                land.setText(emojiList.get(0));
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(durations.get(0)), e -> land.setText(emojiList.get(1))),
                        new KeyFrame(Duration.seconds(durations.get(1)), e -> land.setText(emojiList.get(2)))
                );

                timeline.setOnFinished(e -> {
                    this.collectAuthorized = true;
                });

                timeline.play();
            }
    };
}
