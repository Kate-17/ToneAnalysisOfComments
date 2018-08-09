package javaFX;

import YouTube.*;
import NaiveBayes.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

public class Controller {
   public enum type { videoId, channelId}
   public static type typeOfResoruce;

    @FXML private PieChart pieChart;

    @FXML private TextField tfID;

    @FXML private TextArea txtAreaOfPositive,txtAreaOfNegative,txtAreaOfNeitral;
    public static TextArea textAreaOfPositive,textAreaOfNegative,textAreaOfNeitral;

    @FXML private Button btnChannel,btnVideo;

    @FXML public Label lblResource,lbl_NameOfChannel,lblNameOfVideosOrCountOfVideos,lblOfOfLikesOrSubscribers,
            lblOfDislikesOrViews,lblNoSuch;
    public static Label lbl__NameOfChannel;


    @FXML private TextField ref_OfChannel;

    @FXML private Label lbl_NameOfVideoOrCountOfChannel,lbl_CountOfLikesOrSubscribers,lbl_CountOfDislikesOrViews,
            lbl_AmountOfComputeComments;


    public static boolean flag=false;
    @FXML
    void initialize(){
        ref_OfChannel.setEditable(false);
        Controller.textAreaOfPositive = txtAreaOfPositive;
        Controller.textAreaOfNegative = txtAreaOfNegative;
        Controller.textAreaOfNeitral = txtAreaOfNeitral;
        Controller.lbl__NameOfChannel = lbl_NameOfChannel;

        typeOfResoruce = type.videoId;
    }

    @FXML
    void onBtnClear(){
        tfID.setText("");
        lbl_NameOfChannel.setText("");
        ref_OfChannel.setText("");
        lbl_NameOfVideoOrCountOfChannel.setText("");
        lbl_CountOfLikesOrSubscribers.setText("");
        lbl_CountOfDislikesOrViews.setText("");
        lbl_AmountOfComputeComments.setText("");
        lblNoSuch.setText("");
        textAreaOfNegative.setText("");
        textAreaOfNeitral.setText("");
        textAreaOfPositive.setText("");
        pieChart.setVisible(false);
    }


    void pieChartShow(){
        lbl_AmountOfComputeComments.setText(String.valueOf(Classification.n+Classification.p+Classification.ntr));
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        float negPercent=0,posPercent=0,neitPercent=0;
        int u =Classification.n+Classification.p+Classification.ntr;
        if (Classification.n!=0)
            negPercent = Classification.n*100/u;
        if (Classification.p!=0)
            posPercent = Classification.p*100/u;
        if (Classification.ntr!=0)
            neitPercent = Classification.ntr*100/u;

        PieChart.Data one = new PieChart.Data("Отрицательных "+negPercent+"% ("+Classification.n+")", negPercent);
        PieChart.Data two = new PieChart.Data("Нейтральных "+neitPercent+"% ("+Classification.ntr+")", neitPercent);
        PieChart.Data three = new PieChart.Data("Положительных "+posPercent+"%0 ("+Classification.p+")",posPercent);

        if (Classification.n!=0)
            data.add(one);
        if (Classification.ntr!=0)
            data.add(two);
        if (Classification.p!=0)
            data.add(three);

        pieChart.setData(data);
        pieChart.setLabelsVisible(false);
        pieChart.setVisible(true);
    }

    @FXML
    void onBtnOK() throws Exception {
        lbl_NameOfChannel.setText("");
        ref_OfChannel.setText("");
        lbl_NameOfVideoOrCountOfChannel.setText("");
        lbl_CountOfLikesOrSubscribers.setText("");
        lbl_CountOfDislikesOrViews.setText("");
        lbl_AmountOfComputeComments.setText("");
        lblNoSuch.setText("");
        pieChart.setVisible(false);
        textAreaOfNegative.setText("");
        textAreaOfNeitral.setText("");
        textAreaOfPositive.setText("");

        if (typeOfResoruce == type.videoId) {
            VideoInfo infoAboutVideo = CommentsFromYouTube.getInfoAboutVideo(tfID.getText());
            if (infoAboutVideo.getTitle() != "      НЕ НАЙДЕНО!           ") {
                lbl_NameOfVideoOrCountOfChannel.setText(infoAboutVideo.getTitle());
                lbl_NameOfChannel.setText(infoAboutVideo.getChannelTitle());
                ref_OfChannel.setText("https://www.youtube.com/channel/" + infoAboutVideo.getIdChannel());
                lbl_CountOfLikesOrSubscribers.setText(infoAboutVideo.getLikes());
                lbl_CountOfDislikesOrViews.setText(infoAboutVideo.getDisLikes());
                CommentsFromYouTube.getComments(type.videoId, tfID.getText());
                pieChartShow();
            } else lblNoSuch.setText(infoAboutVideo.getTitle());
        } else if ((typeOfResoruce == type.channelId)) {
            СhannelInfo infoAboutChannel = CommentsFromYouTube.getInfoAboutChannel(tfID.getText());
            if (infoAboutChannel.getTitleOfChannel() != "      НЕ НАЙДЕНО!           ") {
                lbl_NameOfChannel.setText(infoAboutChannel.getTitleOfChannel());
                ref_OfChannel.setText("https://www.youtube.com/channel/"+infoAboutChannel.getIdChannel());
                lbl_NameOfVideoOrCountOfChannel.setText(infoAboutChannel.getVideoCount());
                lbl_AmountOfComputeComments.setText(infoAboutChannel.getCommentCount());
                lbl_CountOfLikesOrSubscribers.setText(infoAboutChannel.getSubscriberCount());
                lbl_CountOfDislikesOrViews.setText(infoAboutChannel.getViewCount());
                CommentsFromYouTube.getComments(type.channelId, tfID.getText());
                pieChartShow();
            } else lblNoSuch.setText(infoAboutChannel.getTitleOfChannel());
        }
        flag=false;
    }

    @FXML
    void onBtnVideo(ActionEvent event){
        lblResource.setText("Видео:");
        tfID.setText("");
        lbl_NameOfChannel.setText("");
        ref_OfChannel.setText("");
        lblNameOfVideosOrCountOfVideos.setText("Название видео:");
        lbl_NameOfVideoOrCountOfChannel.setText("");
        lblOfOfLikesOrSubscribers.setText("Количество лайков:");
        lbl_CountOfLikesOrSubscribers.setText("");
        lbl_CountOfLikesOrSubscribers.setLayoutX(210);
        lblOfDislikesOrViews.setText("Количество дизлайков:");
        lbl_CountOfDislikesOrViews.setText("");
        lbl_CountOfDislikesOrViews.setLayoutX(240);
        lbl_AmountOfComputeComments.setText("");
        lblNoSuch.setText("");
        pieChart.setVisible(false);
        textAreaOfNegative.setText("");
        textAreaOfNeitral.setText("");
        textAreaOfPositive.setText("");

        typeOfResoruce = type.videoId;
        btnChannel.setDisable(false);
        btnVideo.setDisable(true);
    }


    @FXML
    void onDeleteNoSuch(){
        lblNoSuch.setText("");
    }

    @FXML
    void onBtnChannel(ActionEvent event){
        lblResource.setText("Канал:");
        tfID.setText("");
        lbl_NameOfChannel.setText("");
        ref_OfChannel.setText("");
        lblNameOfVideosOrCountOfVideos.setText("Количество видео:");
        lbl_NameOfVideoOrCountOfChannel.setText("");
        lblOfOfLikesOrSubscribers.setText("Количество подписчиков:");
        lbl_CountOfLikesOrSubscribers.setText("");
        lbl_CountOfLikesOrSubscribers.setLayoutX(260);
        lblOfDislikesOrViews.setText("Количество просмотров:");
        lbl_CountOfDislikesOrViews.setText("");
        lbl_CountOfDislikesOrViews.setLayoutX(250);
        lbl_AmountOfComputeComments.setText("");
        lblNoSuch.setText("");
        textAreaOfNegative.setText("");
        textAreaOfNeitral.setText("");
        textAreaOfPositive.setText("");

        pieChart.setVisible(false);
        typeOfResoruce = type.channelId;
        btnChannel.setDisable(true);
        btnVideo.setDisable(false);
    }
}

