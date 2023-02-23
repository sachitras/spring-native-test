package com.example.myshell;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.eclipse.jgit.api.Git;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class BitbucketFileDownloader {

    public static void main(String[] args) {
        try {
//			invokeBitbucketURL();
//			bitTest();
            readBitbucketFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkOutGitFile() {
        try {
            HttpResponse<String> response = Unirest
                    .get("https://raw.githubusercontent.com/sachitras/shell-demo/main/pom.xml").asString();
            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void invokeBitbucketURL() throws UnsupportedEncodingException {
        String username = "sachitrau";
        //String appToken = "ATATT3xFfGF0XV8BJUx0N5IZ8FhWLqMEc095_GUV0LBJC8oyYN7XLU-ZYL_z67Zf4u4550KmVmIxaHP47PTZLNWYEhZkxjHqfRFCJbWIam6HjVtxaZCwVHkcuNH9uiYcgcyNfvEI-uFoPpFVB_xdSoD22SRUhailvDfuPqEuN24K888It-Y3Ml4=26ED5B67";
        String appToken = "ATBBRGzUxngaGrTaJJ22spFecxBS7E80F178";
//		String password = "password@1qaz";
        String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + appToken).getBytes("UTF-8")); // Bitbucket
        String repositoryName = "remote-file-access";
        String branchName = "main";
        String filename = "README.md";

        getFileContentFromRepository(username, repositoryName, branchName, filename, encodedCredentials);
    }

    private static void getFileContentFromRepository(String username, String repositoryName, String branchName,
                                                     String filename, String encodedCredentials) {
        // https://bitbucket.org/sachitrau/remote-file-access/src/main/README.md
        try {

//			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            String urlToAccess = "https://bitbucket.org/" + username + "/" + repositoryName + "/src/" + branchName + "/" + filename;
//			urlToAccess = "https://bitbucket.org/sachitrau/remote-file-access/raw/c877222522dff3d8998cdf31a6f7c4026103ffe3/README.md";
            URL repositoryUrl = new URL(urlToAccess);

//			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            HttpURLConnection connection = (HttpURLConnection) repositoryUrl.openConnection();
            // For authentication
            connection.addRequestProperty("Authorization", "Basic " + encodedCredentials);
            connection.setRequestMethod("GET");
            connection.connect();
//			System.out.println(connection.getResponseCode()+" "+connection.getResponseMessage());
            // The InputStream is required to read in the data of the GET request
            InputStream connectionDataStream = connection.getInputStream();
            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(
                    new InputStreamReader(connectionDataStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }
            String connectionStreamData = textBuilder.toString();
            System.out.println(connectionStreamData);
            // the data can be returned as well, just change the return type of the function
            // to String.

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void bitTest() {
        try {

            String username = "sachitrau";
            String appToken = "ATATT3xFfGF0XV8BJUx0N5IZ8FhWLqMEc095_GUV0LBJC8oyYN7XLU-ZYL_z67Zf4u4550KmVmIxaHP47PTZLNWYEhZkxjHqfRFCJbWIam6HjVtxaZCwVHkcuNH9uiYcgcyNfvEI-uFoPpFVB_xdSoD22SRUhailvDfuPqEuN24K888It-Y3Ml4=26ED5B67";
            String encodedCredentials = Base64.getEncoder()
                    .encodeToString((username + ":" + appToken).getBytes("UTF-8")); // Bitbucket

            HttpResponse<String> response = Unirest.get(
                            "https://bitbucket.org/sachitrau/remote-file-access/raw/c877222522dff3d8998cdf31a6f7c4026103ffe3/README.md")
                    .basicAuth("sachitrau", encodedCredentials)
                    .asString();
            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readBitbucketFile() throws MalformedURLException, IOException {
        ArrayList<String> lines = new ArrayList<>();

        String username = "sachitrau";
        String apppassword = "ATBBRGzUxngaGrTaJJ22spFecxBS7E80F178";
        String reponame = "remote-file-access";
        byte[] encodedAuth = Base64.getEncoder().encode((username+":"+apppassword).getBytes());


//		HttpURLConnection connection = (HttpURLConnection) new URL("https://api.bitbucket.org/2.0/repositories/"+username+"/"+reponame+"").openConnection();
//		connection.setRequestProperty("Authorization", "Basic " + new String(encodedAuth));
//		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		JSONObject lastestCommit = new JSONObject(new JSONObject(reader.readLine()).getJSONArray("values").get(0).toString());

        HttpURLConnection connection = (HttpURLConnection) new URL("https://bitbucket.org/"+username+"/"+reponame+"/src/main/README.md").openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        connection.setRequestProperty("Authorization", "Basic " + new String(encodedAuth));
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        System.out.println(lines.toString());
        connection.disconnect();
    }

}
