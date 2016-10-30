package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mey on 9/26/2016.
 */
public class Config {
    //TODO create later a config file .config to read the setup of this project
    private static Config config;
    private String initDate;
    private String finalDate;
    private String projectPath;
    private String jiraIdPattern;
    private String jiraUrl;




    public String getInitDate() {
        return initDate;
    }
    public String getFinalDate() {
        return finalDate;
    }

    private Config(){
        Properties studyProperties = new Properties();
        InputStream input  = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            studyProperties.load(input);
            this.initDate=studyProperties.getProperty("initDate");
            this.finalDate=studyProperties.getProperty("finalDate");
            this.projectPath=studyProperties.getProperty("projectPath");
            this.jiraIdPattern=studyProperties.getProperty("jiraIdPattern");
            this.jiraUrl=studyProperties.getProperty("jiraUrl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstace() {
        if(config==null){
            config = new Config();
            return config;
        }
        else{
            return config;
        }
    }

    public String getProjectPath(){
        return projectPath;
    }

    public String getJiraIssuePattern() {
        return this.jiraIdPattern;
    }

    public String getJiraUrl() {
        return this.jiraUrl;
    }
}
