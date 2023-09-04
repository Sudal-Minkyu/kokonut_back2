package com.app.kokonut.common;

import com.slack.api.Slack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackUtil {

    private static String kokonutState;

    private static String slackToken;

    private static String slackChannel;

    @Value("${kokonut.state}")
    public void setKokonutState(String kokonutState) {
        SlackUtil.kokonutState = kokonutState;
    }

    @Value("${kokonut.slack.token}")
    public void setSlackToken(String slackToken) {
        SlackUtil.slackToken = slackToken;
    }

    @Value("${kokonut.slack.channel}")
    public void setSlackChannel(String slackChannel) {
        SlackUtil.slackChannel = slackChannel;
    }

    public static void registerAlarmSend(String message) {

        if(kokonutState.equals("3")) {
            message = "[회원가입] 코코넛에서 전송 \n"+message;
        }else if((kokonutState.equals("2"))) {
            message = "[회원가입] 베타 서버(내부용)에서 전송 \n"+message;
        } else {
            message = "[회원가입] 개발 서버(내부용)에서 전송 \n"+message;
        }

        try {
            String finalMessage = message;
            Slack.getInstance().methods().chatPostMessage(req -> req
                    .token(slackToken)
                    .channel(slackChannel) // 채널 이름
                    .text(finalMessage) // 메시지 내용
            );
        } catch (Exception e) {
            log.error("슬랙 메세지 전송 에러 : "+e.getMessage());
        }
    }

    public static void onbordingAlarmSend(String message) {

        if(kokonutState.equals("3")) {
            message = "[온보딩신청] 코코넛에서 전송 \n"+message;
        }else if((kokonutState.equals("2"))) {
            message = "[온보딩신청] 베타 서버(내부용)에서 전송 \n"+message;
        } else {
            message = "[온보딩신청] 개발 서버(내부용)에서 전송 \n"+message;
        }

        try {
            String finalMessage = message;
            Slack.getInstance().methods().chatPostMessage(req -> req
                    .token(slackToken)
                    .channel(slackChannel) // 채널 이름
                    .text(finalMessage) // 메시지 내용
            );
        } catch (Exception e) {
            log.error("슬랙 메세지 전송 에러 : "+e.getMessage());
        }
    }

}
