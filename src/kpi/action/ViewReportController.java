package kpi.action;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;
 
 
public class ViewReportController extends Window implements AfterCompose
{
    private static final long serialVersionUID = 1L;
    private Iframe iframe;
 
    public void afterCompose()
    {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
    }
 
    public void onClick$btReport()
    {
        String url = "/run?__report=test1.rptdesign&formatRapport=doc";
        iframe.setSrc(url);
    }
   
    public void onClick$btView()
    {
        String url = "/run?__report=test1.rptdesign&formatRapport=html";
        iframe.setSrc(url);
    }
 
}