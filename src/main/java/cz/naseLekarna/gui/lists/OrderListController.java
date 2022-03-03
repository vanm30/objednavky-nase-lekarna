package cz.naseLekarna.gui.lists;

import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Order;
import cz.naseLekarna.system.Storage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * Class loads orders and lists them in home screen
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class OrderListController implements Initializable {


    private static OrderListController orderListController;
    @FXML
    public VBox activeOrders;

    FirebaseService firebaseService = new FirebaseService();
    Storage storage = Storage.getStorage();

    public OrderListController() {
        orderListController = this;
    }

    public static OrderListController getOrderListController() {
        return orderListController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            firebaseService.loadOrders();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!storage.getActiveOrders().isEmpty()) {
            List<Order> list = storage.getActiveOrders();

            for (Order order : list) {
                HBox hBox = null;
                try {
                    hBox = FXMLLoader.load(getClass().getResource("/fxml/orderItem.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activeOrders.getChildren().add(hBox);
                final Label name = (Label) hBox.lookup("#name");
                final Label date = (Label) hBox.lookup("#date");
                final Label orderPickUpInfo = (Label) hBox.lookup("#orderPickUpInfo");
                name.setText(order.getCustomer().getName());
                orderPickUpInfo.setText(order.getOrderPickupInfo().toUpperCase(Locale.ROOT));
                String dateEnd = order.getDateEnd();
                String dateEndDisplay = dateEnd.substring(8,10).replaceFirst("^0+(?!$)", "") + "." + dateEnd.substring(5,7).replaceFirst("^0+(?!$)", "") + "." + dateEnd.substring(0,4);
                date.setText(dateEndDisplay);

                if (!order.getOrderedReceptList().isEmpty()) {
                    final VBox vBox = (VBox) hBox.lookup("#listRecept");
                    for (int i = 0; i < order.getOrderedReceptList().size(); i++) {
                        HBox hBoxItem = null;
                        try {
                            hBoxItem = FXMLLoader.load(getClass().getResource("/fxml/orderItemRecept.fxml"));
                            vBox.getChildren().add(hBoxItem);
                            final Label labelCode = (Label) hBoxItem.lookup("#code");
                            labelCode.setText(String.valueOf(order.getOrderedReceptList().get(i).getCode()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    final VBox vBox = (VBox) hBox.lookup("#listRecept");
                        HBox hBoxItem = null;
                        try {
                            hBoxItem = FXMLLoader.load(getClass().getResource("/fxml/orderItemRecept.fxml"));
                            vBox.getChildren().add(hBoxItem);
                            final Label labelCode = (Label) hBoxItem.lookup("#code");
                            labelCode.setOpacity(0.5);
                            labelCode.setText("Žádné recepty.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                if (!order.getOrderedPripravekList().isEmpty()) {
                    final VBox vBox = (VBox) hBox.lookup("#listPripravek");
                    for (int i = 0; i < order.getOrderedPripravekList().size(); i++) {
                        HBox hBoxItem = null;
                        try {
                            hBoxItem = FXMLLoader.load(getClass().getResource("/fxml/orderItemPripravek.fxml"));
                            vBox.getChildren().add(hBoxItem);
                            final Label labelName = (Label) hBoxItem.lookup("#name");
                            final Label labelAmount = (Label) hBoxItem.lookup("#amount");
                            labelName.setText(String.valueOf(order.getOrderedPripravekList().get(i).getName()));
                            labelAmount.setText(String.valueOf(order.getOrderedPripravekList().get(i).getAmount() + " ks"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    final VBox vBox = (VBox) hBox.lookup("#listPripravek");
                    HBox hBoxItem = null;
                    try {
                        hBoxItem = FXMLLoader.load(getClass().getResource("/fxml/orderItemPripravek.fxml"));
                        vBox.getChildren().add(hBoxItem);
                        final Label labelName = (Label) hBoxItem.lookup("#name");
                        labelName.setOpacity(0.5);
                        labelName.setText("Žádné přípravky.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                LocalDate today = LocalDate.now();
                LocalDate week = today.plusWeeks(1);
                LocalDate threeDays = today.plusDays(3);
                LocalDate fourDays = today.plusDays(4);
                LocalDate dateEndCompare = LocalDate.parse(order.getDateEnd());
                if ((dateEndCompare.isBefore(fourDays) && dateEndCompare.isAfter(today)) || dateEndCompare.isBefore(today)) {
                    hBox.lookup("#infoBox").setStyle("-fx-background-color: #d3d3d3; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #e03e82; -fx-border-width: 2");
                } else if (dateEndCompare.isBefore(week) && dateEndCompare.isAfter(threeDays)) {
                    hBox.lookup("#infoBox").setStyle("-fx-background-color: #d3d3d3; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ead023; -fx-border-width: 2");
                }
            }
        }
    }

}
