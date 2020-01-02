package org.noear.solon.boot.jdksocket;

import org.noear.solon.XApp;
import org.noear.solon.core.SocketMessage;

import java.io.PrintWriter;

public class SocketContextHandler {
    private XApp xapp;

    public SocketContextHandler(XApp xapp) {
        this.xapp = xapp;
    }

    public void handler(SocketSession session) {
        SocketMessage request = session.getMessage();

        if(request == null){
            return;
        }

        SocketContext context = new SocketContext(session,request);

        try {
            xapp.handle(context);
        } catch (Throwable ex) {
            ex.printStackTrace();
            ex.printStackTrace(new PrintWriter(context.outputStream()));
        }

        try {
            context.commit();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
