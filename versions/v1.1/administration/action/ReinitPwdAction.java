package administration.action;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.sun.xml.internal.bind.v2.TODO;

import administration.model.ReinitPwdModel;

public class ReinitPwdAction extends GenericForwardComposer{
	
	Textbox user;
	Textbox email;
	Label msg;
	Div div;
	Window main;
	
	public void onClick$login(MouseEvent event) throws Exception{
		
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
		}
		
	public void onClick$cancel(MouseEvent event) throws Exception{
		Executions.createComponents("../login/login.zul", div, null);	
		main=(Window)this.self;
		main.detach();
	}

}
