package burp;


import gui.PreferencesPanel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class BurpExtender implements IBurpExtender, IContextMenuFactory, ClipboardOwner, ITab {
    private IExtensionHelpers helpers;

    private final static String NAME = "saramBURP";
    private final static String SENDREQ = "Send Req to saramBURP";
    private final static String SENDREQRES = "Send Req/Res to saramBURP";

    public static PreferencesPanel preferencesPanel;
    public static IBurpExtenderCallbacks publicCallbacks;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        publicCallbacks = callbacks;
        helpers = callbacks.getHelpers();
        callbacks.setExtensionName(NAME);
        callbacks.registerContextMenuFactory(this);
        preferencesPanel = new PreferencesPanel();
        callbacks.addSuiteTab(this);

    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        final IHttpRequestResponse[] messages = invocation.getSelectedMessages();
        if (messages == null || messages.length == 0) return null;
        JMenuItem i = new JMenuItem(SENDREQ);
        i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData(messages, true);
            }
        });
        JMenuItem j = new JMenuItem(SENDREQRES);
        j.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData(messages, false);
            }
        });
        List<JMenuItem> itemList = Arrays.asList(i, j);
        return itemList;
    }

    private void sendData(IHttpRequestResponse[] messages, Boolean onlyReq) {
        StringBuilder py = new StringBuilder();


        for (IHttpRequestResponse message : messages) {
            byte[] req = message.getRequest();
            py.append(new String(req));
            if (!onlyReq) {
                byte[] res = message.getResponse();
                py.append("\n\n==============================\n\n");
                py.append(new String(res));
            }

        }


        /* json = {
    	    'id': str(uuid1()), // this needs to be created by Java
	        'type': 'tool',
	        'output': self.output, //req/res goes here
	        'command': 'Burp suite',
	        'user': self.user, //username
	        'comment': ['saramBURP'],
	        'options': {L̥
		        'marked': 2
	        },
	        'time': str(datetime.utcnow()) // Java has to timestamp this
        }*/


        JSONObject json = new JSONObject();
        String uuid = UUID.randomUUID().toString();
        json.put("id", uuid);
        json.put("type", "tool");
        json.put("output", py.toString());
        json.put("command", "Burp suite");

        String username = publicCallbacks.loadExtensionSetting("saram_user");
        json.put("user", username);

        List<String> list = new ArrayList<>();

        list.add("saramBURP");


        json.put("comment", list);

        JSONObject insidejson = new JSONObject();
        insidejson.put("marked", 2);
        json.put("options", insidejson);

        String timestamp = java.time.Instant.now().toString();
        json.put("time", timestamp);


        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        try {

            String uri = publicCallbacks.loadExtensionSetting("saram_url") +
                    "/" + publicCallbacks.loadExtensionSetting("saram_token") + "/";

            BurpExtender.publicCallbacks.issueAlert(uri);

            HttpPatch request = new HttpPatch(uri);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            System.out.println(request);
            HttpResponse response = httpClient.execute(request);

            BurpExtender.publicCallbacks.issueAlert("response is: " + response + " data: " + json.toString());
            System.out.println(response);
            //handle response here...

        } catch (Exception ex) {

            //handle exception here
            BurpExtender.publicCallbacks.issueAlert(ex.toString());

        } finally {
            //Deprecated
            //httpClient.getConnectionManager().shutdown();
        }
    }

    @Override
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
    }

    @Override
    public String getTabCaption() {
        return NAME;
    }

    @Override
    public Component getUiComponent() {
        return preferencesPanel.getPanel();
    }
}