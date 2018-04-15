package common;
import java.io.IOException;
import java.util.Properties;

public class InitContext
{

    public String user;
    public String password;
    public String host;
    public String port;
    public String socketFactoryport;
    public String from;
    public String to;
    public String text;
    public String smtpsubject;
    public String jdbcurl;
    public String jdbcusername;
    public String jdbcpassword;
    public Properties props;
    public String smtpsubject_alert;
    public String cc;
    public String dbtype;
    public String server;
    public String actionDev_alert;
    

	public static final String DIRECTORY_CONNECTION_PARAMETERS = "resources.properties";
    
    public String timerValue ;

    public InitContext()
    {
        props = new Properties();
    }

    public void loadProperties()
    {
        try
        {
        	
            props.load(getClass().getResourceAsStream("resources.properties"));
            setUser(props.getProperty("smtp.user"));
            setPassword(props.getProperty("smtp.password"));
            setHost(props.getProperty("smtp.host"));
            setPort(props.getProperty("smtp.port"));
            setSocketFactoryport(props.getProperty("smtp.socketFactory.port"));
            setFrom(props.getProperty("smtp.from"));
            setTo(props.getProperty("smtp.to"));
            setText(props.getProperty("smtp.text"));
            setSmtpSubject(props.getProperty("smtp.subject"));
            setJdbcpassword(props.getProperty("jdbc.password"));
            setJdbcusername(props.getProperty("jdbc.username"));
            setJdbcurl(props.getProperty("jdbc.url"));
            setSmtpsubject_alert(props.getProperty("alert.subject"));
            setDbtype(props.getProperty("jdbc.dbType"));
            setServer(props.getProperty("smtp.server"));
            
            //information sur la durée d'une evaluation en minutes
            setTimerValue(props.getProperty("timer.value"));
            setCc(props.getProperty("smtp.cc"));
            
            setActionDev_alert(props.getProperty("actionDev.subject"));
                        
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getSocketFactoryport()
    {
        return socketFactoryport;
    }

    public void setSocketFactoryport(String socketFactoryport)
    {
        this.socketFactoryport = socketFactoryport;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getSmtpSubject()
    {
        return smtpsubject;
    }

    public void setSmtpSubject(String subject)
    {
        smtpsubject = subject;
    }

    public static String getDIRECTORY_CONNECTION_PARAMETERS()
    {
        return "resources.properties";
    }

    public String getJdbcurl()
    {
        return jdbcurl;
    }

    public void setJdbcurl(String jdbcurl)
    {
        this.jdbcurl = jdbcurl;
    }

    public String getJdbcusername()
    {
        return jdbcusername;
    }

    public void setJdbcusername(String jdbcusername)
    {
        this.jdbcusername = jdbcusername;
    }

    public String getJdbcpassword()
    {
        return jdbcpassword;
    }

    public void setJdbcpassword(String jdbcpassword)
    {
        this.jdbcpassword = jdbcpassword;
    }

	public String getSmtpsubject_alert() {
		return smtpsubject_alert;
	}

	public void setSmtpsubject_alert(String smtpsubject_alert) {
		this.smtpsubject_alert = smtpsubject_alert;
	}

	public String getTimerValue() {
		return timerValue;
	}

	public void setTimerValue(String timerValue) {
		this.timerValue = timerValue;
	}
    
   /* public static void main(String arg[]){
    	InitContext init =new InitContext();
    	init.loadProperties();
    } */  
    
	  public String getCc()
	    {
	        return cc;
	    }

	    public void setCc(String cc)
	    {
	        this.cc = cc;
	    }

		public String getDbtype() {
			return dbtype;
		}

		public void setDbtype(String dbtype) {
			this.dbtype = dbtype;
		}
		public String getServer() {
			return server;
		}

		public void setServer(String server) {
			this.server = server;
		}

		public String getActionDev_alert() {
			return actionDev_alert;
		}

		public void setActionDev_alert(String actionDev_alert) {
			this.actionDev_alert = actionDev_alert;
		}  
		
		
		
	    
    
}
