package administration.model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import administration.bean.SelCliBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import common.CreateDatabaseCon;
import common.InitContext;
import common.PwdCrypt;

public class ReinitPwdModel {

	private List  user_login_email;

	public boolean checkLoginEmailValidity(String login) throws SQLException{


		boolean result=false;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		user_login_email= new ArrayList();
		ResultSet rs=null;
		int type_result=0;
		try {
			stmt = (Statement) conn.createStatement();
			String user_login="select 1 from compte where login=#login";
			user_login = user_login.replaceAll("#login", "'"+login.toUpperCase()+"'");

			//System.out.println(select_login);
			rs = (ResultSet) stmt.executeQuery(user_login);


			while(rs.next()){
				if (rs.getRow()==1  ){
					result= true;
				}
				else{ result= false;}

			}
			//			stmt.close();
			//			conn.close();

		} catch ( SQLException e ) {

		} finally {

			if ( rs != null ) {
				try {
					rs.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
		return result;	



	}

	public void sendReinitPwdConfirmation(String recipient,String newpwd){
		final InitContext intctx = new InitContext();
		intctx.loadProperties();
		Properties props = new Properties();

		/*props.put("mail.smtp.host", intctx.getHost());
	props.put("mail.smtp.socketFactory.port", intctx.getPort());
	props.put("mail.smtp.socketFactory.class",
			"javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", intctx.getPort());*/

		/*props.setProperty("smtp.airaldgerie.dz", "localhost");
	props.put("mail.smtp.host", "mail.airalgerie.dz");
	props.put("mail.smtp.user", "evalcom@airalgerie.dz");
	props.put("mail.smtp.password", "trf123");
	props.put("mail.smtp.port", "25");*/

		//props.setProperty("smtp.airaldgerie.dz", "localhost");
		props.setProperty(intctx.getServer(), "localhost");
		props.put("mail.smtp.host", intctx.getHost());
		props.put("mail.smtp.user", intctx.getUser());
		props.put("mail.smtp.password", intctx.getPassword());
		props.put("mail.smtp.port", intctx.getPort());

		Session session=Session.getDefaultInstance(props);





		/*Session session = Session.getDefaultInstance(props,
		new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(intctx.getUser(),intctx.getPassword());
			}
		});*/

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(intctx.getUser()));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipient));
			message.setSubject(intctx.getSmtpSubject());
			message.setText(intctx.getText()+". Votre nouveau mot de passe est:  "+newpwd);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
	//generate a random password having 8 bits 
	public String generateNewPwd(){
		int rawRandomNumber;
		int min = 11111111;
		int max = 99999999;

		rawRandomNumber = (int) (Math.random() * (max - min + 1) ) + min;
		//System.out.println("Random number : " + rawRandomNumber);

		return Integer.toString(rawRandomNumber);
	}

	public void updateInDBNewPwd(String login,String newpwd) throws SQLException{
		boolean result=false;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		PwdCrypt pwdcrypt=new PwdCrypt();

		//calculate a new password


		//give today datetime 	
		Date today = Calendar.getInstance().getTime();
		// (2) create our "formatter" (our custom format)
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// (3) create a new String in the format we want
		String todaydate = formatter.format(today);

		try {
			stmt = (Statement) conn.createStatement();
			String user_login="update compte set pwd=#pwd,modifiedpwd=#date where upper(login)=#login";
			user_login = user_login.replaceAll("#login", "'"+login.toUpperCase()+"'");
			user_login=user_login.replaceAll("#pwd","'"+ pwdcrypt.crypter(newpwd)+"'");
			user_login=user_login.replaceAll("#date","'"+ todaydate+"'");

			//System.out.println(select_login);
			stmt.executeUpdate(user_login);
			//		stmt.close();
			//		conn.close();



		} catch ( SQLException e ) {

		} finally {


			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}


	}

	public int checkInDBLoginOldPwd(String login,String ancienPwdstr) throws SQLException{
		boolean result=false;
		CreateDatabaseCon dbcon=new CreateDatabaseCon();
		Connection conn=(Connection) dbcon.connectToPrincipalDB();
		Statement stmt = null;
		PwdCrypt pwdcrypt=new PwdCrypt();
		String retour=null;
		int  retour_int=0;



		try {

			
			stmt = (Statement) conn.createStatement();
			String selectSQL = "select pwd from common_evalcom.compte where upper(login) = upper(#login)";
			selectSQL = selectSQL.replaceAll("#login", "'"+login.toUpperCase()+"'");
			
		    ResultSet rs = stmt.executeQuery(selectSQL);
			while (rs.next()) {
				retour = rs.getString("pwd");

			}
			
			if (retour !=null){
				
				String pwd_decry=pwdcrypt.decrypter(retour);
				if (pwd_decry.equalsIgnoreCase(ancienPwdstr)){
					retour_int=1;
					 
				}
				
			}
			
		
			

		} catch ( SQLException e ) {
			System.out.println(e.toString());

		} finally {


			if ( stmt != null ) {
				try {
					stmt.close();
				} catch ( SQLException ignore ) {
				}
			}

			if ( conn != null ) {
				try {
					conn.close();
				} catch ( SQLException ignore ) {
				}
			}
		}
 return retour_int;

	}

}
