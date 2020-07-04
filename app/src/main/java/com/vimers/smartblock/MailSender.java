package com.vimers.smartblock;
import co.nedim.maildroidx.MaildroidX;
import co.nedim.maildroidx.MaildroidXType;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MailSender {
    private String text;
    private String title;
    private String to;

    MailSender(String title, String text, String to) {
        this.title = title;
        this.text = text;
        this.to = to;
    }

    public void sendMail() {
        new MaildroidX.Builder()
            .smtp("smtp.gmail.com")
            .smtpUsername("jj4971221@gmail.com")
            .smtpPassword("Nikita!1290")
            .port("465")
            .type(MaildroidXType.HTML)
            .to(to)
            .from("no-reply@gmail.com")
            .subject(title)
            .body(text)
            .isJavascriptDisabled(false)
            .mail();
    }
}
