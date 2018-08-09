import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trainingSetFromTwitter.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("javaFX/MainForm.fxml"));
        primaryStage.setTitle("Тоновая классификация комментариев");
        primaryStage.setScene(new Scene(root, 1100, 650));
        primaryStage.show();

    }


    public static void main(String[] args) throws Exception {
        launch(args);
      //  SmileAndDeleteForLoadOnPlatform.prepareText("Веломастерская Велолаб вам в помощь. https://www.youtube.com/channel/UCiGWNHgOMZj19cSm7DvMlvg");
    }
}
