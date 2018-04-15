package administration.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.Version;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import administration.model.LoginModel;
import common.ApplicationFacade;
import common.ApplicationSession;


public class Login extends GenericForwardComposer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button login;
	Window main;
	Div   loginDiv;
	Div   userDiv;
	Label msg;
	Label userName;
	Textbox usertb;
	Textbox pwdtb;
	org.zkoss.zul.Borderlayout borderly;
	LoginModel init =new LoginModel();
	SelCliAction initselcli=new SelCliAction();
	Div div;




	void showMsg(){
		String message = "You are running ZK Successfully, The ZK Version is "+Version.UID;
		alert(message);
	}

	public void onClick$login(MouseEvent event) throws Exception
	{
		String user = usertb.getValue();
		String pwd = pwdtb.getValue();



		if(Strings.isBlank(user) || Strings.isEmpty(pwd)){
			msg.setValue("Merci de saisir votre login et mot de passe!");
			return;
		}


		try 
		{
			//msg.setValue("before checklogin");
			int result=init.checkLoginPwd(user,pwd);
			//msg.setValue("after checklogin");
			if (result==0) 
			{
				//msg.setValue("authentifié");
				Map data = new HashMap();

				data.put("name", user);
				data.put("age", pwd);
				//msg.setValue("before user compte");
				//chargement des informations associés au profil de 'utilisateur
				init.checkProfile(init.getUser_compte());
				/* recuperation de la database id par utilisateur
				 * Pour le super utilisateur le database id est recuperer
				 * aprer la selection de la base via l'ecran SELCLI
				 */
				//msg.setValue("after user compte");
				ApplicationSession applicationSession=(ApplicationSession)Sessions.getCurrent().getAttribute("APPLICATION_ATTRIBUTE");
				if(applicationSession==null)
					applicationSession=new ApplicationSession();
				applicationSession.setClient_database_id(init.getDatabase_id());
				//Session  session=Sessions.getCurrent();


				Sessions.getCurrent().setAttribute("APPLICATION_ATTRIBUTE", applicationSession);

				//TODO delete commentaire ApplicationFacade
				//ApplicationFacade.getInstance().setClient_database_id(init.getDatabase_id());


				//System.out.println("AVANT"+ApplicationFacade.getInstance().getClient_database_id());
				if (init.getProfile_id()==1){
					Executions.createComponents("../pages/SELCLI.zul", div, data);	
					main=(Window)this.self;
					main.detach();

				}
				else{

					Executions.createComponents("../pages/menu.zul", div, data);
					//permet de fermer la fenetre login
					main=(Window)this.self;
					main.detach();

				}



			} 
			else if (result==1) 
			{ 
				msg.setValue("utilisateur ou mot de passe erroné");
			}
			else if (result==2) 
			{ 
				msg.setValue(" login expiré.Merci de contacter l'administrateur");
			}
		} 




		catch (SQLException e) 
		{
			// TODO Auto-generated catch block

			msg.setValue(e.toString());
		}

		/*session.setAttribute("user",user);
		loginDiv.setVisible(false);
		userDiv.setVisible(true);
		userName.setValue(user);
		msg.setValue("");*/
		//        Map data = new HashMap();
		//     
		//		data.put("name", "111");
		//		data.put("age", "lll");
		//		System.out.println("vant de rendre invisible");
		//		
		//		main.setVisible(false);
		//		Executions.createComponents("/index.zul",null, data);


	}
	public void doLogout(){
		session.removeAttribute("user");

		loginDiv.setVisible(true);
		userDiv.setVisible(false);
		userName.setValue("");
	}
	public void doOK(){
		if(loginDiv.isVisible()){
			//onClick$login();
		}else{
			doLogout();
		}
	}

	//String user = (String)session.getAttribute("user");

}
