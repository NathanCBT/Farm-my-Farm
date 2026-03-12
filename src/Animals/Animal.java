package Animals;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

public abstract class Animal {
    public String name;
    public String productName;
    public double buyMoney;
    public double productPrice;
    public int productionTime;
    public String emojiAnimal;
    public String emojiProduct;
    public String foodNeeded;

    public boolean canCollect = false;
    public boolean isProducing = false;
    public boolean isHungry = true;

        public void produce(Button pen) {
            // on ne produit que si l'animal n'a plus faim et ne produit pas déjà
            if (!isHungry && !canCollect && !isProducing) {
                isProducing = true;
                pen.setText(emojiAnimal + " ⏳");

                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(productionTime), e -> {
                            pen.setText(emojiProduct);
                            this.canCollect = true;
                            this.isProducing = false;
                            this.isHungry = true; // après avoir donné une ressource à l'animal il faudra lui en donner une autre
                        })
                );
                timeline.play();
            } else if (isHungry) {
                pen.setText(emojiAnimal + " 😋?");
        }
    }
}
