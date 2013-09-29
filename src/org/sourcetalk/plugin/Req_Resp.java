/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sourcetalk.plugin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author ling
 */
public class Req_Resp {
    
    static public String sendPost(String targetURL, String urlParameters) throws MalformedURLException, IOException, URISyntaxException  {
        URL url; 
        url = new URL(targetURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", 
             "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Length", "" + 
                 Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");  

        connection.setUseCaches (false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //Send request
        DataOutputStream wr = new DataOutputStream (
                             connection.getOutputStream ());
        wr.writeBytes (urlParameters);
        wr.flush ();

        //Get Response	
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuilder response = new StringBuilder(); 
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }

        
        return connection.getURL().toString();
    }
    
}
