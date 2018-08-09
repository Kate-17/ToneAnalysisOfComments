package YouTube;

public class VideoInfo {

    public VideoInfo(String Title, String IdChannel, String ChannelTitle, String Likes, String DisLikes, String CommentCount) {
        this.Title = Title;
        this.IdChannel = IdChannel;
        this.ChannelTitle = ChannelTitle;
        this.Likes = Likes;
        this.DisLikes = DisLikes;
        this.CommentCount = CommentCount;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIdChannel() {
        return IdChannel;
    }

    public void setIdChannel(String idChannel) {
        IdChannel = idChannel;
    }

    String Title;
    String IdChannel;

    public String getChannelTitle() {
        return ChannelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.ChannelTitle = channelTitle;
    }

    String ChannelTitle;
    String Likes;

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getDisLikes() {
        return DisLikes;
    }

    public void setDisLikes(String disLikes) {
        DisLikes = disLikes;
    }

    String DisLikes;

    public String getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(String commentCount) {
        CommentCount = commentCount;
    }

    String CommentCount;
}
