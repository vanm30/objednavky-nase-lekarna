package cz.naseLekarna.gui;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.VBox;

/**
 * @author Matěj Vaník
 * @created 03.02.2022
 */
public class OrderOpener {

    private static OrderOpener orderOpener;
    public VBox listPripravek;
    public VBox listRecept;

    public OrderOpener() {
        orderOpener = this;
    }

    public static OrderOpener getOrderOpener() {
        return orderOpener;
    }


    public void openOrder(MouseEvent mouseEvent) {
        /*TODO*/
    }

    public void openOrdertoo(TouchEvent touchEvent) {
    }
}
