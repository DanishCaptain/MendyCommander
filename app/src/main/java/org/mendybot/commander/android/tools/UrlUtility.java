package org.mendybot.commander.android.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtility {

    private static final int TIME_OUT = 2000;

    private UrlUtility() {
    }


    public static void doIt() {
        String urlString = "https://";
        try {
            URL url = new URL(urlString);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(TIME_OUT);
            client.setDoInput(true);
            client.setDoOutput(true);
            client.setInstanceFollowRedirects(false);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "text/json");
            client.setRequestProperty("charset", "utf-8");
            client.connect();


            client.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String exchangeJson(String urlString, String json) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection client = null;
        try {
            URL url = new URL(urlString);
            client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(TIME_OUT);
            client.setDoInput(true);
            client.setDoOutput(true);
            client.setInstanceFollowRedirects(false);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "text/json");
            client.setRequestProperty("charset", "utf-8");

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            pw.println(json);
            pw.flush();
            pw.close();

            if (client.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line;
                while((line = br.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }
                br.close();
            } else {
                System.out.println(client.getResponseCode());
            }

            client.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public static String grabJson(String urlString) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection client = null;
        try {
            URL url = new URL(urlString);
            client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(TIME_OUT);
            client.setDoInput(true);
            client.setDoOutput(false);
            client.setInstanceFollowRedirects(false);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "text/json");
            client.setRequestProperty("charset", "utf-8");

            if (client.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line;
                while((line = br.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }
                br.close();
            } else {
                System.out.println(client.getResponseCode());
            }

            client.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public static void putJson(String urlString, String json) {
        HttpURLConnection client = null;
        try {
            URL url = new URL(urlString);
            client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(TIME_OUT);
            client.setDoInput(true);
            client.setDoOutput(false);
            client.setInstanceFollowRedirects(false);
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "text/json");
            client.setRequestProperty("charset", "utf-8");

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            pw.println(json);
            pw.flush();
            pw.close();

            if (client.getResponseCode() == 200) {
                System.out.println(client.getResponseCode());
            } else {
                System.out.println(client.getResponseCode());
            }

            client.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
