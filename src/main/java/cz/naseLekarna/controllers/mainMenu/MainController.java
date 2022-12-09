package cz.naseLekarna.controllers.mainMenu;

import cz.naseLekarna.controllers.Animation;
import cz.naseLekarna.controllers.lists.CustomerListController;
import cz.naseLekarna.controllers.lists.OrderListController;
import cz.naseLekarna.controllers.newOrder.FindCustomerController;
import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.Order;
import cz.naseLekarna.system.Storage;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Matěj Vaník
 * @created 09.11.2021
 */

public class MainController implements Initializable {

    private static MainController mainController;

    public MainController() {
        mainController = this;
    }

    public static MainController getMainController() {
        return mainController;
    }

    @FXML
    public Button menuButton;
    @FXML
    public GridPane gridPane;
    @FXML
    public StackPane mainStackPane;
    @FXML
    public Label mainLabel;
    @FXML
    public Button removeMenu;
    @FXML
    public TextField searchBar;
    @FXML
    public Button searchButton;
    private boolean menuOpened;

    Storage storage = Storage.getStorage();
    Animation animation = new Animation();

    /**
     * This method is called when inilializing this Controller. It puts homeView.fxml to main stack pane.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox vBox = null;
        try {
            vBox = FXMLLoader.load(getClass().getResource("/views/mainMenu/homeView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(vBox);
        removeMenu.setVisible(false);
        searchBar.setVisible(false);
    }

    public void changeScene(String fxml) {
        mainStackPane.getChildren().clear();
        Node node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/views/" + fxml + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(node);
    }

    /**
     * This method opens popup menu when pressing button.
     *
     * @throws Exception
     */
    public void openMenu() throws Exception {
        if (menuOpened) return;
        menuOpened = true;

        VBox vBox = FXMLLoader.load(getClass().getResource("/views/mainMenu/menu.fxml"));
        mainStackPane.getChildren().add(vBox);
        TranslateTransition slide = animation.slideMenuIn(vBox);
        slide.setOnFinished(event -> {
            removeMenu.setVisible(true);
            menuButton.setVisible(false);
        });
    }

    /**
     * This method closes popup menu.
     */
    public void closeMenu() throws IOException, InterruptedException {
        if (!menuOpened) return;
        menuOpened = false;
        VBox vBox = (VBox) mainStackPane.lookup("#menuBox");
        TranslateTransition slide = animation.slideOut(vBox);
        slide.setOnFinished(event -> {
            mainStackPane.getChildren().remove(vBox);
            removeMenu.setVisible(false);
            menuButton.setVisible(true);
        });
    }

    /**
     * This method opens home view.
     *
     * @throws IOException
     */
    public void switchToHome() throws IOException, InterruptedException {
        //close menu
        closeMenu();
        // load new view (home)
        mainController.changeScene("mainMenu/homeView");
    }
    /**
     * This method opens customers list.
     *
     * @throws IOException
     */
    public void switchToCustomers() throws IOException, InterruptedException {
        //close menu
        closeMenu();
        //load new view (customers)
        mainController.changeScene("lists/customerList");
    }

    /**
     * This method opens settings.
     *
     * @throws IOException
     */
    public void switchToSettings() throws IOException, InterruptedException {
        //close manu
        closeMenu();
        //load new view (settings)
        mainController.changeScene("mainMenu/settings");
    }

    public void startSearch(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (mainLabel.isVisible()) {
            if (menuOpened) closeMenu();
            mainLabel.setVisible(false);
            searchBar.setVisible(true);
            searchBar.requestFocus();

        }else {
            if (mainStackPane.getChildren().get(0).getId() != null && mainStackPane.getChildren().get(0).getId().equals("orders") && !searchBar.getText().isEmpty()){
                List<Order> list = storage.getActiveOrders();
                OrderListController.getOrderListController().loadOrders(list);
            }
            if (mainStackPane.getChildren().get(0).getId() != null && mainStackPane.getChildren().get(0).getId().equals("findCustomers") && !searchBar.getText().isEmpty()){
                FindCustomerController.getCustomerListController().loadCustomers();
            }
            if (mainStackPane.getChildren().get(0).getId() != null && mainStackPane.getChildren().get(0).getId().equals("customersList") && !searchBar.getText().isEmpty()){
                CustomerListController.getCustomerListController().loadCustomers();
            }
            searchBar.clear();
            mainLabel.setVisible(true);
            searchBar.setVisible(false);
        }
    }


    public void search() throws IOException {
        if (mainStackPane.getChildren().get(0).getId() == null){
            return;
        }
        if (mainStackPane.getChildren().get(0).getId().equals("orders")) {
            if (searchBar.getText().isEmpty()) {
                List<Order> list = storage.getActiveOrders();
                OrderListController.getOrderListController().loadOrders(list);
            } else if (!storage.getActiveOrders().isEmpty()) {
                ArrayList<String> names = storage.orderNames;
                ArrayList<Integer> numbers = storage.orderNumbers;
                String search = searchBar.getText().toLowerCase();
                Set<Object> searchedNames = new HashSet<Object>();

                for (Object name : names) {
                    if (name.toString().toLowerCase().contains(search)) {
                        searchedNames.add(name);
                    }
                }
                for (Object number : numbers) {
                    if (search.equals(number.toString())) {
                        searchedNames.add(number);
                    }
                }
                if (!searchedNames.isEmpty()) {
                    OrderListController.getOrderListController().searchOrders(searchedNames);
                } else {
                    OrderListController.getOrderListController().activeOrders.getChildren().clear();
                    Label label = new Label();
                    label.setText("Nezalezeny žádné objednávky.");
                    label.setId("searchError");
                    OrderListController.getOrderListController().activeOrders.getChildren().add(label);
                }
            }
        }
        if (mainStackPane.getChildren().get(0).getId().equals("findCustomers")) {
            if (searchBar.getText().isEmpty()) {
                FindCustomerController.getCustomerListController().loadCustomers();
            } else if (!storage.getActiveCustomers().isEmpty()) {
                String search = searchBar.getText().toLowerCase();
                Set<Customer> searchedCustomers = new HashSet<>();

                for (Customer customer : storage.getActiveCustomers()) {
                    if (customer.getName().toLowerCase().contains(search)) searchedCustomers.add(customer);
                    if (customer.getPhoneNumber().toLowerCase().toString().equals(search)) searchedCustomers.add(customer);
                    if (!(customer.getStreet() == null)) {
                        if (customer.getStreet().toLowerCase().contains(search)) searchedCustomers.add(customer);
                    }
                    if (!(customer.getCity() == null)) {
                        if (customer.getCity().toLowerCase().contains(search)) searchedCustomers.add(customer);
                    }
                }

                if (!searchedCustomers.isEmpty()) {
                    FindCustomerController.getCustomerListController().searchCustomers(searchedCustomers);
                } else {
                    FindCustomerController.getCustomerListController().customers.getChildren().clear();

                }
            }
        }
        if (mainStackPane.getChildren().get(0).getId().equals("customersList")) {
            if (searchBar.getText().isEmpty()) {
                CustomerListController.getCustomerListController().loadCustomers();
            } else if (!storage.getActiveCustomers().isEmpty()) {
                String search = searchBar.getText().toLowerCase();
                Set<Customer> searchedCustomers = new HashSet<>();

                for (Customer customer : storage.getActiveCustomers()) {
                    if (customer.getName().toLowerCase().contains(search)) searchedCustomers.add(customer);
                    if (customer.getPhoneNumber().toLowerCase().toString().equals(search)) searchedCustomers.add(customer);
                    if (!(customer.getStreet() == null)) {
                        if (customer.getStreet().toLowerCase().contains(search)) searchedCustomers.add(customer);
                    }
                    if (!(customer.getCity() == null)) {
                        if (customer.getCity().toLowerCase().contains(search)) searchedCustomers.add(customer);
                    }
                }

                if (!searchedCustomers.isEmpty()) {
                    CustomerListController.getCustomerListController().searchCustomers(searchedCustomers);
                } else {
                    CustomerListController.getCustomerListController().customers.getChildren().clear();
                    Label label = new Label();
                    label.setText("Nenalezeny žádní zákazníci");
                    label.setId("empty");
                    CustomerListController.getCustomerListController().customers.getChildren().add(label);
                }
            }
        }
    }
}
