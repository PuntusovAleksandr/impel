package it.feio.android.omninotes.utils;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import roboguice.util.Ln;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class ACRAPostSender implements ReportSender {

    private final static String BASE_URL = Constants.ACRA_MAILER_URL + Constants.DEV_EMAIL;
    private final static String SHARED_SECRET = "my_shared_secret";
    private Map<String, String> custom_data = null;


    ACRAPostSender() {
    }


    public ACRAPostSender(HashMap<String, String> custom_data) {
        this.custom_data = custom_data;
    }


    @Override
    public void send(CrashReportData report) throws ReportSenderException {

        String url = getUrl();
        Ln.i("Sending crash report to " + url);

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();

            if (custom_data != null) {
                for (Map.Entry<String, String> entry : custom_data.entrySet()) {
                    parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            parameters.add(new BasicNameValuePair("DATE", new Date().toString()));
            parameters.add(new BasicNameValuePair("USER_COMMENT", report.get(ReportField.USER_COMMENT)));
            parameters.add(new BasicNameValuePair("REPORT_ID", report.get(ReportField.REPORT_ID)));
            parameters.add(new BasicNameValuePair("APP_VERSION_CODE", report.get(ReportField.APP_VERSION_CODE)));
            parameters.add(new BasicNameValuePair("APP_VERSION_NAME", report.get(ReportField.APP_VERSION_NAME)));
            parameters.add(new BasicNameValuePair("PACKAGE_NAME", report.get(ReportField.PACKAGE_NAME)));
            parameters.add(new BasicNameValuePair("PHONE_MODEL", report.get(ReportField.PHONE_MODEL)));
            parameters.add(new BasicNameValuePair("ANDROID_VERSION", report.get(ReportField.ANDROID_VERSION)));
            parameters.add(new BasicNameValuePair("BRAND", report.get(ReportField.BRAND)));
            parameters.add(new BasicNameValuePair("CUSTOM_DATA", report.get(ReportField.CUSTOM_DATA)));
            parameters.add(new BasicNameValuePair("STACK_TRACE", report.get(ReportField.STACK_TRACE)));
            parameters.add(new BasicNameValuePair("CRASH_CONFIGURATION", report.get(ReportField.CRASH_CONFIGURATION)));
            parameters.add(new BasicNameValuePair("DISPLAY", report.get(ReportField.DISPLAY)));
            parameters.add(new BasicNameValuePair("SHARED_PREFERENCES", report.get(ReportField.SHARED_PREFERENCES)));

            httpPost.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
            httpClient.execute(httpPost);
        } catch (Exception e) {
            Ln.e(e, "Error sending crash report");
        }
    }


    private String getUrl() {
        String token = getToken();
        String key = getKey(token);
        return String.format("%s&token=%s&key=%s&", BASE_URL, token, key);
    }


    private String getKey(String token) {
        return md5(String.format("%s+%s", SHARED_SECRET, token));
    }


    private String getToken() {
        return md5(UUID.randomUUID().toString());
    }


    public static String md5(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}

