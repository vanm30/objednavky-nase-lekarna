package cz.naseLekarna.gui.lists;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Order;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Class loads orders and lists them in home screen
 *
 * @author Matěj Vaník
 * @created 24.11.2021
 */
public class OrderListController implements Initializable {


    private static OrderListController orderListController;

    public OrderListController() {
        orderListController = this;
    }

    public static OrderListController getOrderListController() {
        return orderListController;
    }

    @FXML
    public VBox activeOrders;
    @FXML
    public ScrollPane scrollPane;

    FirebaseService firebaseService = new FirebaseService();
    Storage storage = Storage.getStorage();

    /**
     * This method lists all active orders.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        try {
            firebaseService.loadOrders();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        List<Order> list = storage.getActiveOrders();
        loadOrders(list);
    }


    public void loadOrders(List<Order> list) {
        activeOrders.getChildren().clear();

        if (!list.isEmpty()) {
            for (Order order : list) {
                HBox hBox = null;
                try {
                    hBox = FXMLLoader.load(getClass().getResource("/fxml/editOrder/orderItem.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activeOrders.getChildren().add(hBox);
                final Label name = (Label) hBox.lookup("#name");
                final Label date = (Label) hBox.lookup("#date");
                final Label orderID = (Label) hBox.lookup("#orderID");
                final Label orderPickUpInfo = (Label) hBox.lookup("#orderPickUpInfo");
                final Label orderNumber = (Label) hBox.lookup("#orderNumber");
                final Label state = (Label) hBox.lookup("#state");
                final Node dateAn = hBox.lookup("#dateAn");
                if (order.getCustomer().getName().isEmpty()) {
                    name.setText("-");
                } else name.setText(Validator.sanitizeHTML(order.getCustomer().getName()));
                orderPickUpInfo.setText(Validator.sanitizeHTML(order.getOrderPickupInfo()));
                String datePickUp = order.getDatePickUp().format(DateTimeFormatter.ofPattern("d. M. y"));
                date.setText(Validator.sanitizeHTML(datePickUp));
                if (order.orderNumber != null) {
                    orderNumber.setText(Validator.sanitizeHTML(order.getOrderNumber().toString()));
                } else orderNumber.setText("-");
                orderID.setText(Validator.sanitizeHTML(order.getOrderId()));
                if (!order.getState().isEmpty()) {
                    state.setText(Validator.sanitizeHTML(order.getState()));
                }

                if (!order.getOrderedReceptList().isEmpty()) {
                    final VBox vBox = (VBox) hBox.lookup("#listRecept");
                    for (int i = 0; i < order.getOrderedReceptList().size(); i++) {
                        HBox hBoxItem = null;
                        try {
                            hBoxItem = FXMLLoader.load(getClass().getResource("/fxml/orderItemRecept.fxml"));
                            vBox.getChildren().add(hBoxItem);
                            final Label labelCode = (Label) hBoxItem.lookup("#code");
                            labelCode.setText(Validator.sanitizeHTML(String.valueOf(order.getOrderedReceptList().get(i).getCode())));
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
                            labelName.setText(Validator.sanitizeHTML(String.valueOf(order.getOrderedPripravekList().get(i).getName())));
                            labelAmount.setText(Validator.sanitizeHTML(String.valueOf(order.getOrderedPripravekList().get(i).getAmount() + " ks")));
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
                LocalDate threeDays = today.plusDays(3);
                if (order.getDatePickUp().isEqual(today)) {
                    dateAn.setStyle("-fx-background-radius: 100;-fx-background-color: red");
                } else if (order.getDatePickUp().isBefore(threeDays) && order.getDatePickUp().isAfter(today)) {
                    dateAn.setStyle("-fx-background-radius: 100;-fx-background-color: yellow");
                } else if (order.getDatePickUp().isBefore(today)) {
                    dateAn.setStyle("-fx-background-radius: 100;-fx-background-color: orange");
                }

                if (state.getText().equals("Připraveno")){
                    state.setTextFill(Paint.valueOf("green"));
                }else if (state.getText().equals("Objednáno")){
                    state.setTextFill(Paint.valueOf("yellow"));
                }else if (state.getText().equals("Neobjednáno")){
                    state.setTextFill(Paint.valueOf("red"));
                }
            }
        } else{
            Label label = new Label();
            label.setText("Žádné objednávky");
            label.setId("empty");
            activeOrders.getChildren().add(label);
        }
    }

    public void searchOrders(Set<Object> searched) {
        ArrayList<Order> list = new ArrayList<>();
            if (searched != null) {
                for (Object object : searched) {
                    if (Validator.isNumeric(object.toString())) {
                        for (Order order : storage.getActiveOrders()) {
                            if (order.getOrderNumber().equals(object)) {
                                list.add(order);
                            }
                        }
                    } else {
                        for (Order order : storage.getActiveOrders()) {
                            if (order.getCustomer().getName().equals(object)) {
                                list.add(order);
                            }
                        }
                    }
                }
            }
            loadOrders(list);
        }

    }

