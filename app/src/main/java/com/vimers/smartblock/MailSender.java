package com.vimers.smartblock;

import co.nedim.maildroidx.MaildroidX;
import co.nedim.maildroidx.MaildroidXType;

public class MailSender {
    private final String text;
    private final String title;
    private final String to;

    MailSender(String title, String text, String to) {
        this.title = title;
        this.text = text;
        this.to = to;
    }

    //Sends mail to a (to) address with (title) header and (text) main body
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
