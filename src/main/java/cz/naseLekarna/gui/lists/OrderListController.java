package cz.naseLekarna.gui.lists;

import cz.naseLekarna.gui.mainMenu.MainController;
import cz.naseLekarna.system.FirebaseService;
import cz.naseLekarna.system.Order;
import cz.naseLekarna.system.Storage;
import cz.naseLekarna.system.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        loadOrders();
    }


    public void loadOrders() {
        activeOrders.getChildren().clear();
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
                if (order.getCustomer().getName().isEmpty()) {
                    name.setText("-");
                } else name.setText(order.getCustomer().getName());
                orderPickUpInfo.setText(order.getOrderPickupInfo());
                String dateEnd = order.getDateEnd().format(DateTimeFormatter.ofPattern("d. M. y"));
                date.setText(dateEnd);
                if (order.orderNumber != null) {
                    orderNumber.setText(order.getOrderNumber().toString());
                } else orderNumber.setText("-");
                orderID.setText(order.getOrderId());

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
                LocalDate dateEndCompare = order.getDateEnd();
                if ((dateEndCompare.isBefore(fourDays) && dateEndCompare.isAfter(today)) || dateEndCompare.isBefore(today) || dateEndCompare.isEqual(today)) {
                    hBox.lookup("#infoBox").setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #e03e82; -fx-border-width: 2");
                } else if (dateEndCompare.isBefore(week) && dateEndCompare.isAfter(threeDays)) {
                    hBox.lookup("#infoBox").setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ead023; -fx-border-width: 2");
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
        activeOrders.getChildren().clear();
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
                if (order.getCustomer().getName().isEmpty()) {
                    name.setText("-");
                } else name.setText(order.getCustomer().getName());
                orderPickUpInfo.setText(order.getOrderPickupInfo());
                String dateEnd = order.getDateEnd().format(DateTimeFormatter.ofPattern("d.M.y")); //TODO
                date.setText(String.valueOf(order.getDateEnd()));
                orderNumber.setText(order.getOrderNumber().toString());
                orderID.setText(order.getOrderId());

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
                LocalDate dateEndCompare = order.getDateEnd();
                if ((dateEndCompare.isBefore(fourDays) && dateEndCompare.isAfter(today)) || dateEndCompare.isBefore(today) || dateEndCompare.isEqual(today)) {
                    hBox.lookup("#infoBox").setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #e03e82; -fx-border-width: 2");
                } else if (dateEndCompare.isBefore(week) && dateEndCompare.isAfter(threeDays)) {
                    hBox.lookup("#infoBox").setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ead023; -fx-border-width: 2");
                }
            }
        }

    }

