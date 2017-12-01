package framework.common.functions;

import framework.Logger;
import framework.utils.MailUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import javax.mail.*;

public final class MailFunctions {
    public static String getUnlockLink(String login, String password, String folder, String subject) throws Exception {
        String htmlPage = getMessageAndDelete(login, password, folder, subject);
        Document html = Jsoup.parse(htmlPage);
        return html.body().getElementsByAttributeValue("name", "CTA").attr("href");
    }

    public static String getUnlockLink(String login, String password) throws Exception {
        String folder = "INBOX";
        String subject = "Forgot your password?";
        return getUnlockLink(login, password, folder, subject);
    }

    private static String getMessageAndDelete(String login, String password, String folder, String subject) throws Exception {
        MailUtils mail = new MailUtils(login, password);
        Folder fold = mail.getStoreConnected().getFolder(folder);
        fold.open(Folder.READ_WRITE);
        Logger.getInstance().info(String.format("connection with email is: %s", mail.isConnected()));
        Message message = mail.waitForLetter(folder, subject);
        Multipart multipart = (Multipart) message.getContent();
        StringBuilder htmlPage = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            htmlPage.append(multipart.getBodyPart(i).getContent().toString());
        }
        message.setFlag(Flags.Flag.DELETED, true);
        fold.close(true);
        return htmlPage.toString();
    }
}
