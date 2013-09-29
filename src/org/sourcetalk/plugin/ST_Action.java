/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sourcetalk.plugin;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import org.openide.cookies.EditorCookie;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "org.sourcetalk.plugin.ST_Action")
@ActionRegistration(
        displayName = "#CTL_ST_Action")
@ActionReference(path = "Menu/Tools", position = 1800, separatorBefore = 1750)
@Messages("CTL_ST_Action=Send to SourceTalk.net")
public final class ST_Action implements ActionListener {

    private final EditorCookie context;

    public ST_Action(EditorCookie context) {
        this.context = context;
        
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        JEditorPane currentPane = context.getOpenedPanes()[0];
        String title=(String) context.getDocument().getProperty("title");
        File file = new File(title);
        String name = file.getName();

        final String code = currentPane.getText();
        int num= getRow(currentPane.getCaretPosition(), currentPane);
        
        String  urlParameters = null;
        try {
            urlParameters = "conference[file_name]="
                +URLEncoder.encode(name,"UTF-8")+"&"
                +"conference[source]="
                +URLEncoder.encode(code,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        }
        try {       
            String open_url = Req_Resp.sendPost("http://app.sourcetalk.net/conferences",urlParameters);
            
            open_url += "/" + num;
            Desktop.getDesktop().browse(new URL(open_url).toURI());
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
    @SuppressWarnings("empty-statement")
       
    public static int getRow(int pos, JEditorPane editor) {
        int rowNumber= (pos==0) ? 1 : 0;
        try {
            int offs=pos;
            while( offs>0) {
                offs=Utilities.getRowStart(editor, offs)-1;
                rowNumber++;
            }
        } catch (BadLocationException e) {
        }
        return rowNumber;
    }
}
