package utils;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;

public class JiraReader {
    public static boolean IsBug(String issueId){
        try {
            String url = "https://issues.apache.org/jira/rest/api/2/issue/" + issueId;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (response.toString().toLowerCase().contains("bug")){
                return true;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return false;
    }
}
