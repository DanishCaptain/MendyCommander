package org.mendybot.commander.android.tools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;

public final class UrlUtility {

    private static final int TIME_OUT = 2000;
    private static final String TAG = UrlUtility.class.getSimpleName();

    private UrlUtility() {
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
                Log.d(TAG, "response code: "+client.getResponseCode());
            }

            client.disconnect();
        } catch (MalformedURLException e) {
            Log.w(TAG, e);
        } catch (IOException e) {
            Log.w(TAG, e);
        } finally {
            if (client != null) {
                try {
                    client.connect();
                } catch (IOException e) {
                    Log.w(TAG, e);
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
                Log.d(TAG, "response code: "+client.getResponseCode());
            }

            client.disconnect();
        } catch (MalformedURLException e) {
            Log.w(TAG, e);
            result.append("[]");
        } catch (NoRouteToHostException e) {
            Log.w(TAG, e);
            result.append("[]");
        } catch (IOException e) {
            Log.w(TAG, e);
            result.append("[]");
        } finally {
            if (client != null) {
                client.disconnect();
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
                Log.d(TAG, "response code: "+client.getResponseCode());
            } else {
                Log.d(TAG, "response code: "+client.getResponseCode());
            }

            client.disconnect();
        } catch (MalformedURLException e) {
            Log.w(TAG, e);
        } catch (IOException e) {
            Log.w(TAG, e);
        } finally {
            if (client != null) {
                try {
                    client.connect();
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            }
        }
    }

    public static String grabHtml(String urlString) {
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
            client.setRequestProperty("Content-Type", "text/html");
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
                Log.d(TAG, "response code: "+client.getResponseCode());
            }

            client.disconnect();
        } catch (MalformedURLException e) {
            Log.w(TAG, e);
            result.append("[]");
        } catch (NoRouteToHostException e) {
            Log.w(TAG, e);
            result.append("[]");
        } catch (IOException e) {
            Log.w(TAG, e);
            result.append("[]");
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
        return result.toString();
    }
}
