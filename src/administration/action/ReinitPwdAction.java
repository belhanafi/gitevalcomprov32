package administration.action;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

//import com.sun.xml.internal.bind.v2.TODO;


import administration.model.ReinitPwdModel;

public class ReinitPwdAction extends GenericForwardComposer{

	Textbox user;
	Textbox ancienPwd;
	Textbox nouveauPwd;
	Textbox confnouveauPwd;
	Popup info_admin;
	Popup info_admin1;


	Label msg;
	Div div;
	Window main;

	/*public void onClick$login(MouseEvent event) throws Exception{

		String userstr = user.getValue();
		String emailstr = email.getValue();
		ReinitPwdModel init=new ReinitPwdModel();


		///Messagebox.show("Hello, " + event.getName());
		if(Strings.isBlank(userstr) || Strings.isEmpty(emailstr)){
			msg.setValue("Merci de saisir les champs vides");
			return;
		}

		else if(emailstr.indexOf("@")==-1){
			msg.setValue("L'adresse mail saisie  "+emailstr+"  n'a pas le format de l'adresse mail valide");
			return;
		}

		else if (init.checkLoginEmailValidity(userstr)){
				 String newpwd=init.generateNewPwd();
				 init.updateInDBNewPwd(userstr, newpwd);
				 init.sendReinitPwdConfirmation(emailstr, newpwd);
				 msg.setValue("Le nouveau mot de passe a été envoyé à l'adresse mail  "+emailstr);
			 }

			 //TODO send an email to the user 

			return;
		}*/

	
	
	
	public void onClick$aide(MouseEvent event) {
		info_admin.open(80, 80);
		
	}
	
	public void onClick$aide1(MouseEvent event) {
		info_admin1.open(80, 80);
		
	}
	public void onClick$login(MouseEvent event) throws Exception{

		String userstr = user.getValue();
		String ancienPwdstr = ancienPwd.getValue();
		String nouveauPwdStr = nouveauPwd.getValue();
		String confnouveauPwdStr = confnouveauPwd.getValue();

		ReinitPwdModel init=new ReinitPwdModel();


		///Messagebox.show("Hello, " + event.getName());
		if(Strings.isBlank(userstr) || Strings.isEmpty(ancienPwdstr)||Strings.isEmpty(nouveauPwdStr)||Strings.isEmpty(confnouveauPwdStr)){
			msg.setValue("Merci de saisir les champs vides");
			return;
		}


		else if(nouveauPwdStr.equalsIgnoreCase(ancienPwdstr) ){
			msg.setValue("Merci de choisir un mot de passe different de l'ancien");
			return;
		}

		
		else if(nouveauPwdStr.length()!=8|| confnouveauPwdStr.length()!=8 ){
			msg.setValue("Merci de choisir un mot de passe avec 8 caractères");
			return;
		}

		else if(! nouveauPwdStr.equalsIgnoreCase(confnouveauPwdStr)){
			msg.setValue("Merci de saisir le meme mot de passe  dans le champ nouveau et confirme nouveau mot de passe");
			return;
		}

		//Tester si le login entré est un login valid
		else if (init.checkInDBLoginOldPwd(userstr,ancienPwdstr) !=1){
			msg.setValue("L'utilisateur ou le mot de passe saisi n'est pas correct");
			return;
		}

		else{
			if ( Messagebox.show( "Voulez vous modifier votre mot de passe?", "Prompt", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION ) == Messagebox.YES )
			{
				init.updateInDBNewPwd(userstr, nouveauPwdStr);
				
				 Messagebox.show("Mot de passe modifié avec  succès", "Information", Messagebox.OK, Messagebox.INFORMATION);
				
				
				Executions.createComponents("../login/login.zul", div, null);	
				main=(Window)this.self;
				main.detach();
				//return;
			}

		}	//TODO send an email to the user 

			return;
		}

		public void onClick$cancel(MouseEvent event) throws Exception{
			Executions.createComponents("../login/login.zul", div, null);	
			main=(Window)this.self;
			main.detach();
		}

	}
