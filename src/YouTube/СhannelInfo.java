package YouTube;

public class СhannelInfo {

    public СhannelInfo(String TitleOfChannel, String IdChannel, String VideoCount, String CommentCount, String SubscriberCount, String ViewCount) {
        this.TitleOfChannel = TitleOfChannel;
        this.IdChannel = IdChannel;
        this.VideoCount = VideoCount;
        this.CommentCount = CommentCount;
        this.SubscriberCount = SubscriberCount;
        this.ViewCount = ViewCount;
    }

    String TitleOfChannel;

    public String getTitleOfChannel() {
        return TitleOfChannel;
    }

    public void setTitleOfChannel(String titleOfChannel) {
        TitleOfChannel = titleOfChannel;
    }

    public String getIdChannel() {
        return IdChannel;
    }

    public void setIdChannel(String idChannel) {
        IdChannel = idChannel;
    }

    public String getVideoCount() {
        return VideoCount;
    }

    public void setVideoCount(String videoCount) {
        VideoCount = videoCount;
    }

    public String getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(String commentCount) {
        CommentCount = commentCount;
    }

    String IdChannel;
    String VideoCount;
    String CommentCount;

    public String getSubscriberCount() {
        return SubscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        SubscriberCount = subscriberCount;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

    String SubscriberCount;
    String ViewCount;



}
