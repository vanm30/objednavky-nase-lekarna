package cz.naseLekarna.gui.mainMenu;

import cz.naseLekarna.gui.Animation;
import cz.naseLekarna.gui.lists.CustomerListController;
import cz.naseLekarna.gui.newOrder.FindCustomerController;
import cz.naseLekarna.gui.lists.OrderListController;
import cz.naseLekarna.system.Customer;
import cz.naseLekarna.system.Storage;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

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
            vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(vBox);
        removeMenu.setVisible(false);
        searchBar.setVisible(false);
    }

    /**
     * This method opens popup menu when pressing button.
     *
     * @throws Exception
     */
    public void openMenu() throws Exception {
        if (menuOpened) return;
        menuOpened = true;

        VBox vBox = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/menu.fxml"));
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
        mainStackPane.getChildren().clear();
        VBox homeView = null;
        try {
            homeView = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/homeView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(homeView);
        mainLabel.setText("Aktivní Objednávky");
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
        mainStackPane.getChildren().clear();
        VBox homeView = null;
        try {
            homeView = FXMLLoader.load(getClass().getResource("/fxml/lists/customerList.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(homeView);
        mainLabel.setText("Pacienti");
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
        mainStackPane.getChildren().clear();
        VBox settings = null;
        try {
            settings = FXMLLoader.load(getClass().getResource("/fxml/mainMenu/settings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainStackPane.getChildren().add(settings);
        mainLabel.setText("Nastavení");
    }

    public void startSearch(ActionEvent actionEvent) throws IOException, InterruptedException {
        if (mainLabel.isVisible()) {
            if (menuOpened) closeMenu();
            mainLabel.setVisible(false);
            searchBar.setVisible(true);

        }else {
            if (mainStackPane.getChildren().get(0).getId() != null && mainStackPane.getChildren().get(0).getId().equals("orders") && !searchBar.getText().isEmpty()){
                OrderListController.getOrderListController().loadOrders();
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


    public void search() {
        if (mainStackPane.getChildren().get(0).getId() == null){
            return;
        }
        if (mainStackPane.getChildren().get(0).getId().equals("orders")) {
            if (searchBar.getText().isEmpty()) {
                OrderListController.getOrderListController().loadOrders();
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
                String search = searchBar.getText();
                Set<Customer> searchedCustomers = new HashSet<>();

                for (Customer customer : storage.getActiveCustomers()) {
                    if (customer.getName().contains(search)) searchedCustomers.add(customer);
                    if (customer.getPhoneNumber().toString().equals(search)) searchedCustomers.add(customer);
                    if (!(customer.getStreet() == null)) {
                        if (customer.getStreet().contains(search)) searchedCustomers.add(customer);
                    }
                    if (!(customer.getCity() == null)) {
                        if (customer.getCity().contains(search)) searchedCustomers.add(customer);
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
                String search = searchBar.getText();
                Set<Customer> searchedCustomers = new HashSet<>();

                for (Customer customer : storage.getActiveCustomers()) {
                    if (customer.getName().contains(search)) searchedCustomers.add(customer);
                    if (customer.getPhoneNumber().toString().equals(search)) searchedCustomers.add(customer);
                    if (!(customer.getStreet() == null)) {
                        if (customer.getStreet().contains(search)) searchedCustomers.add(customer);
                    }
                    if (!(customer.getCity() == null)) {
                        if (customer.getCity().contains(search)) searchedCustomers.add(customer);
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
