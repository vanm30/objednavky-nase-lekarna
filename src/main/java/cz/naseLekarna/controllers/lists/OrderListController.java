package cz.naseLekarna.controllers.lists;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
            Comparator<Order> compare = Comparator
                    .comparing(Order::getDatePickUp)
                    .thenComparing(Order::getOrderNumber, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing((Order o)->o.getCustomer().getName(), Comparator.nullsFirst(Comparator.naturalOrder()));

            List<Order> sortedOrders = list.stream()
                    .sorted(compare)
                    .collect(Collectors.toList());

            for (Order order : sortedOrders) {
                Node orderBox = null;
                try {
                    orderBox = FXMLLoader.load(getClass().getResource("/views/editOrder/orderItem.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activeOrders.getChildren().add(orderBox);
                final Label name = (Label) orderBox.lookup("#name");
                final Label date = (Label) orderBox.lookup("#date");
                final Label orderID = (Label) orderBox.lookup("#orderID");
                final Label orderPickUpInfo = (Label) orderBox.lookup("#orderPickUpInfo");
                final Label orderNumber = (Label) orderBox.lookup("#orderNumber");
                final Label state = (Label) orderBox.lookup("#state");
                final Node dateImage = orderBox.lookup("#dateImage");
                if (order.getCustomer().getName().equals("")) {
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

                if (!order.getOrderedPrescriptionList().isEmpty()) {
                    final VBox vBox = (VBox) orderBox.lookup("#listPrescription");
                    try {
                        vBox.getChildren().add(FXMLLoader.load(getClass().getResource("/views/prescriptionLabel.fxml")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < order.getOrderedPrescriptionList().size(); i++) {
                        HBox hBoxItem = null;
                        try {
                            hBoxItem = FXMLLoader.load(getClass().getResource("/views/orderPrescription.fxml"));
                            vBox.getChildren().add(hBoxItem);
                            final Label labelCode = (Label) hBoxItem.lookup("#code");
                            labelCode.setText(Validator.sanitizeHTML(String.valueOf(order.getOrderedPrescriptionList().get(i).getCode())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!order.getOrderedProductList().isEmpty()) {
                    final VBox vBox = (VBox) orderBox.lookup("#listProduct");
                    try {
                        vBox.getChildren().add(FXMLLoader.load(getClass().getResource("/views/productLabel.fxml")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < order.getOrderedProductList().size(); i++) {
                        HBox hBoxItem = null;
                        try {
                            hBoxItem = FXMLLoader.load(getClass().getResource("/views/orderProduct.fxml"));
                            vBox.getChildren().add(hBoxItem);
                            final Label labelName = (Label) hBoxItem.lookup("#name");
                            final Label labelAmount = (Label) hBoxItem.lookup("#amount");
                            labelName.setText(Validator.sanitizeHTML(String.valueOf(order.getOrderedProductList().get(i).getName())));
                            labelAmount.setText(Validator.sanitizeHTML(String.valueOf(order.getOrderedProductList().get(i).getAmount() + " ks")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                LocalDate today = LocalDate.now();
                LocalDate threeDays = today.plusDays(3);
                if (order.getDatePickUp().isEqual(today)) {
                    dateImage.setStyle("-fx-background-radius: 100;-fx-background-color: red");
                } else if (order.getDatePickUp().isBefore(threeDays) && order.getDatePickUp().isAfter(today)) {
                    dateImage.setStyle("-fx-background-radius: 100;-fx-background-color: yellow");
                } else if (order.getDatePickUp().isBefore(today)) {
                    dateImage.setStyle("-fx-background-radius: 100;-fx-background-color: orange");
                }

                if (state.getText().equals("Připraveno")){
                    state.setTextFill(Paint.valueOf("green"));
                }else if (state.getText().equals("Objednáno")){
                    state.setTextFill(Paint.valueOf("orange"));
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
                            if (order.getOrderNumber() == object) {
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

