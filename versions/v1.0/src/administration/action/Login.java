package administration.action;

import java.sql.SQLException;
import java.util.HashMap;

import java.util.Map;

import org.zkoss.Version;

import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.lang.Strings;
import org.zkoss.zul.Div; 

import org.zkoss.zk.ui.Executions;


import common.ApplicationFacade;



import administration.model.LoginModel;


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
	LoginModel init =new LoginModel();
	SelCliAction initselcli=new SelCliAction();
	Div div;
	
/*	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		main.setTitle("Hello ZK");
		main.setBorder("normal");
	}
	
	public void onClick$sayHelloBtn(){
		showMsg();
	}*/
	
	void showMsg(){
		String message = "You are running ZK Successfully, The ZK Version is "+Version.UID;
		alert(message);
	}
	
	public void onClick$login(MouseEvent event) throws Exception
	{
		String user = usertb.getValue();
		String pwd = pwdtb.getValue();
		
		
		//Messagebox.show("Hello, " + event.getName());
		if(Strings.isBlank(user) || Strings.isEmpty(pwd)){
			msg.setValue("*Need user name and password!");
			return;
		}
		
		
		try 
		{
			
			int result=init.checkLoginPwd(user,pwd);
			if (result==0) 
			{
				//msg.setValue("authentifié");
				   Map data = new HashMap();
					
					data.put("name", user);
					data.put("age", pwd);
					
					//chargement des informations associés au profil de 'utilisateur
					init.checkProfile(init.getUser_compte());
					/* recuperation de la database id par utilisateur
					 * Pour le super utilisateur le database id est recuperer
					 * aprer la selection de la base via l'ecran SELCLI
					 */
					ApplicationFacade.getInstance().setClient_database_id(init.getDatabase_id());
			

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
				msg.setValue("utilisateur out mot de passe erroné");
			}
			else if (result==2) 
			{ 
				msg.setValue(" login expiré.Merci de contacter l'administrateur");
			}
		} 
		
		
		
		
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
