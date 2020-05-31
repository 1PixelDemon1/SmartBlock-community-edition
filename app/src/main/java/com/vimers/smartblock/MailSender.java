package com.vimers.smartblock;
import co.nedim.maildroidx.MaildroidX;
import co.nedim.maildroidx.MaildroidXType;

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
        .smtpAuthentication(true)
        .port("465")
        .type(MaildroidXType.HTML)
        .to(to)
        .from("SmartBlock")
        .subject(title)
        .body(text)
        .mail();
    }
}
