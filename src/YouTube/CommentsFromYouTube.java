package YouTube;

import javaFX.*;
import NaiveBayes.*;
import trainingSetFromTwitter.*;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.model.Comment;


import java.io.IOException;
import java.util.List;


public class CommentsFromYouTube {
    static String myAPIKey = "";
    private static YouTube youtube;
    private static YouTube.CommentThreads.List commentThreadsListByRequest;

    public static void getComments(Controller.type nameOfResource, String resource) throws Exception {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {
            }
        }).setApplicationName("youtube-cmdline-search-sample").build();

        commentThreadsListByRequest = youtube.commentThreads().list("snippet").setKey(myAPIKey);

        CommentThreadListResponse response = null;
        if (nameOfResource == Controller.type.videoId) {
            commentThreadsListByRequest.setVideoId(resource);
            response = prepareListRequest(nameOfResource, resource).setKey(myAPIKey).execute();
        } else if (nameOfResource == Controller.type.channelId) {
            commentThreadsListByRequest.setChannelId(resource);
            response = prepareListRequest(nameOfResource, resource).setKey(myAPIKey).execute();
        }

        while (true) {
            String nextPageToken = response.getNextPageToken();
            getCommentsThreads(response.getItems());
            if (nextPageToken == null)
                break;
            if (nameOfResource == Controller.type.videoId)
                response = prepareListRequest(nameOfResource, resource).setVideoId(resource).setPageToken(nextPageToken).setKey(myAPIKey).execute();
            else if (nameOfResource == Controller.type.channelId)
                response = prepareListRequest(nameOfResource, resource).setPageToken(nextPageToken).setKey(myAPIKey).execute();
        }
        commentThreadsListByRequest.clear();
    }

    public static YouTube.CommentThreads.List prepareListRequest(Controller.type nameOfResource, String resource) throws Exception {
        if (nameOfResource == Controller.type.videoId)
            return youtube.commentThreads()
                    .list("snippet,replies")
                    .setVideoId(resource)
                    .setOrder("time");
        else if (nameOfResource == Controller.type.channelId)
            return youtube.commentThreads()
                    .list("snippet,replies")
                    .setAllThreadsRelatedToChannelId(resource)
                    .setOrder("time");
        else return null;
    }

    public static void getCommentsThreads(List<CommentThread> commentThreads) throws Exception {
        commentThreadsListByRequest.setKey("myAPIKey");
        for (CommentThread commentThread : commentThreads) {
            CommentThreadReplies replies = commentThread.getReplies();
            Comment commentTopLevel =  commentThread.getSnippet().getTopLevelComment();
            Classification.addComment(commentTopLevel.getSnippet().getTextOriginal(),
                    SmileAndDeleteForLoadOnPlatform.prepareText(commentTopLevel.getSnippet().getTextOriginal()),
                    commentTopLevel.getSnippet().getVideoId(),commentTopLevel.getSnippet().getAuthorDisplayName(),commentTopLevel.getId(),
                    commentTopLevel.getSnippet().getLikeCount().toString(),commentTopLevel.getSnippet().getAuthorChannelUrl(),
                    commentTopLevel.getSnippet().getAuthorChannelId().toString());
            if (replies!=null)
                for (int i=0;i<replies.getComments().size();i++){
                    Comment commentOfReplies =  replies.getComments().get(i);
                    Classification.addComment(commentOfReplies.getSnippet().getTextOriginal(),
                            SmileAndDeleteForLoadOnPlatform.prepareText(commentOfReplies.getSnippet().getTextOriginal()),
                            commentOfReplies.getSnippet().getVideoId(),commentOfReplies.getSnippet().getAuthorDisplayName(),commentOfReplies.getId(),
                            commentOfReplies.getSnippet().getLikeCount().toString(),commentOfReplies.getSnippet().getAuthorChannelUrl(),
                            commentOfReplies.getSnippet().getAuthorChannelId().toString());
            }
        }

    }

    //Информация о видео
    public static VideoInfo getInfoAboutVideo(String resource) throws Exception {

        YouTube youtube2 = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {
            }
        }).setApplicationName("youtube-cmdline-search-sample").build();

        VideoListResponse videoListResponse = youtube2.videos().
                list("snippet,id,statistics").setId(resource).setKey(myAPIKey).execute();

        List<Video> videoList = videoListResponse.getItems();
        if (videoList.isEmpty()) {
            return new VideoInfo("      НЕ НАЙДЕНО!           ", "", "", "", "", "");
        } else {
            Video video = videoList.get(0);
            return new VideoInfo(video.getSnippet().getTitle(), video.getSnippet().getChannelId(),
                    video.getSnippet().getChannelTitle(), video.getStatistics().getLikeCount().toString(),
                    video.getStatistics().getDislikeCount().toString(),
                    video.getStatistics().getCommentCount().toString());
        }
    }

    //Информация о канале
    public static СhannelInfo getInfoAboutChannel(String resource) throws Exception {

        YouTube youtube3 = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {
            }
        }).setApplicationName("youtube-cmdline-search-sample").build();

        ChannelListResponse channelListResponse = youtube3.channels().
                list("snippet,id,statistics").setId(resource).setKey(myAPIKey).execute();

        List<Channel> channelList = channelListResponse.getItems();
        if (channelList.isEmpty()) {
            return new СhannelInfo("      НЕ НАЙДЕНО!           ", "", "", "","","");
        } else {
            Channel channel = channelList.get(0);
            return new СhannelInfo(channel.getSnippet().getTitle(),channel.getId(),channel.getStatistics().getVideoCount().toString(),
                    channel.getStatistics().getCommentCount().toString(),channel.getStatistics().getSubscriberCount().toString(),
                    channel.getStatistics().getViewCount().toString());
        }
    }
}


