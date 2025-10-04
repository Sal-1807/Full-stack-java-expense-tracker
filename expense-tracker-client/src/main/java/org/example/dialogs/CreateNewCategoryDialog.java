package org.example.dialogs;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.example.models.User;
import org.example.utils.SqlUtil;
import org.example.utils.Utilitie;

public class CreateNewCategoryDialog extends CustomDialog {
    private TextField newCategoryTextField;
    private ColorPicker colorPicker;
    private Button createCategoryButton;


    public CreateNewCategoryDialog(User user) {
        super(user);
        setTitle("Create New Category");
        getDialogPane().setContent(createDialogContentBox());
    }

    private VBox createDialogContentBox(){
        VBox dialogContentBox = new VBox(20);

        dialogContentBox.setStyle("-fx-background-color: #0d340d; -fx-padding: 20;");

        newCategoryTextField = new TextField();
        newCategoryTextField.setPromptText("Enter category name");
        newCategoryTextField.getStyleClass().addAll("text-size-md", "field-background", "text-light-gray");

        colorPicker = new ColorPicker();
        colorPicker.getStyleClass().add("text-size-md");
        colorPicker.setMaxWidth(Double.MAX_VALUE);

        createCategoryButton = new Button("Create");
        createCategoryButton.getStyleClass().addAll("bg-light-blue","text-size-md","text-white");
        createCategoryButton.setMaxWidth(Double.MAX_VALUE);
        createCategoryButton.setOnMouseClicked(new  EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String categoryName = newCategoryTextField.getText();
                String color = Utilitie.getHexColorValue(colorPicker);
                System.out.println(color);

                JsonObject userData = new JsonObject();
                userData.addProperty("id", user.getId());

                JsonObject transactionCategoryData = new JsonObject();
                transactionCategoryData.add("user", userData);
                transactionCategoryData.addProperty("categoryName", categoryName);
                transactionCategoryData.addProperty("categoryColor", color);

                boolean postTransactionCategoryStatus = SqlUtil.postTransactionCategory(transactionCategoryData);
                if(postTransactionCategoryStatus){
                    Utilitie.showAlertDialog(Alert.AlertType.INFORMATION,
                            "Success: Create a Transaction Category");
                }else{
                    Utilitie.showAlertDialog(Alert.AlertType.ERROR,
                            "Error: Failed to create Transaction Category");
                }
            }
        });

        dialogContentBox.getChildren().addAll(newCategoryTextField,colorPicker, createCategoryButton);
        return dialogContentBox;
    }
}
