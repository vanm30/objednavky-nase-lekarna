package cz.naseLekarna.gui;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Animation {

    private static Animation animation = new Animation();

    public Animation() {
        animation = this;
    }

    public static Animation getAnimation() {
        return animation;
    }


    public TranslateTransition slideOut(Node node) throws InterruptedException {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(node);

        slide.setToX(-350);
        slide.play();

        node.setTranslateX(0);
        slide.setOnFinished(event -> {
            if (node.getParent() instanceof Pane)
                ((Pane) node.getParent()).getChildren().remove(node);
        });
        return slide;
    }

    public TranslateTransition slideIn(Node node) throws InterruptedException {
        node.setTranslateX(600);
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(node);

        slide.setToX(0);
        slide.play();

        node.setTranslateX(0);
        return slide;
    }

    public TranslateTransition slideMenuIn(Node node) throws InterruptedException {
        node.setTranslateX(-300);
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(node);

        slide.setToX(0);
        slide.play();

        node.setTranslateX(0);
        return slide;
    }

}
