package burp;

import gui.PreferencesPanel;

import javax.swing.*;
import java.util.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.Toolkit;
import javax.swing.JMenuItem;



public class BurpExtender implements IBurpExtender, IContextMenuFactory, ClipboardOwner, ITab
{
    private IExtensionHelpers helpers;

    private final static String NAME = "saramBURP";
    private final static String SENDREQ = "Send Req to saramBURP";
    private final static String SENDREQRES = "Send Req/Res to saramBURP";

    public static PreferencesPanel preferencesPanel;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        helpers = callbacks.getHelpers();
        callbacks.setExtensionName(NAME);
        callbacks.registerContextMenuFactory(this);
        preferencesPanel = new PreferencesPanel();

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Encrypted",preferencesPanel);
        preferencesPanel.repaint();
        callbacks.customizeUiComponent(tabs);
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

        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(py.toString()), this);
    }

    @Override
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {}

    @Override
    public String getTabCaption() {
        return NAME;
    }

    @Override
    public Component getUiComponent() {
        return preferencesPanel;
    }
}