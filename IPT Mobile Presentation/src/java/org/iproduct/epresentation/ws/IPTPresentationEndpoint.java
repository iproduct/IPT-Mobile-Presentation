/* COPYRIGHT & LICENSE HEADER
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * IPT Mobile Presentation demonstrates interactive mobile presentation
 * and login event notifications using WebSocket, JAX-RS (REST) & jQuery Mobile
 *
 * Copyright (c) 2012 - 2014 IPT - Intellectual Products & Technologies Ltd. 
 * All rights reserved.
 * 
 * E-mail: office@iproduct.org
 * Web: http://iproduct.org/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (the "License") 
 * as published by the Free Software Foundation version 2 of the License.
 * You may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at root directory of this project in file 
 * LICENSE.txt.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * When distributing the software, include this COPYRIGHT & LICENSE HEADER  
 * in each file, and include the License file LICENSE.txt in the root directory
 * of your distributable.
 *
 * GPL Classpath Exception:
 * IPT - Intellectual Products & Technologies (IPT) designates this particular 
 * file as subject to the "Classpath" exception as provided by IPT in 
 * the GPL Version 2 License file that accompanies this code.
 * 
 * In case you modify this file,
 * please add the appropriate notice below the existing Copyright notices, 
 * with the fields enclosed in brackets {} replaced by your own identification:
 * "Portions Copyright (c) {year} {name of copyright owner}"
 */

package org.iproduct.epresentation.ws;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.iproduct.epresentation.ws.decoders.PresentationMessageDecoder;
import org.iproduct.epresentation.ws.encoders.PresentationMessageEncoder;
import org.iproduct.epresentation.ws.message.PresentationMessage;
import org.iproduct.epresentation.ws.utility.UserKind;
import static org.iproduct.epresentation.ws.message.PresentationMessage.*;

/**
 * This class represents WebSocket server endpoint and handles  
 * different types of messages
 *
 * @author Trayan Iliev
 */
@ServerEndpoint(value = "/ws", decoders = {PresentationMessageDecoder.class},
    encoders = {PresentationMessageEncoder.class})
public class IPTPresentationEndpoint {

    private static Set<Session> sessions = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    /**
     * This method handles different types of incoming messages and 
     * sends/broadcasts responses to all interested parties
     * @param message the message received by server endpoint
     * @param session the WebSocket session
     */
    @OnMessage
    public void onMessage(PresentationMessage message, Session session) {
        String sessionId = message.getSessionId();
        String name;
        try{
            switch (message.getType()) {
                case LOGIN_ACTION:
                    name = message.getPayload().getString("name", "Anonimous");
                    session.getUserProperties().put("name", name);
                    if (name.equalsIgnoreCase("Trayan")) {
                        session.getUserProperties().put("userKind", UserKind.PRESENTER);
                    } else {
                        session.getUserProperties().put("userKind", UserKind.VIEWER);
                    }
                    Logger.getLogger(IPTPresentationEndpoint.class.getName()).log(Level.INFO, name 
                        + " has successfully logged in as "
                        + session.getUserProperties().get("userKind"));
                    session.getBasicRemote().sendObject(
                        new PresentationMessage(LOGIN_RESPONSE, session.getId(), 
                            Json.createObjectBuilder().add("message", "You are logged as " + name).build()));
                    sendMessageToOthers(session, USER_ONLINE_RESPONSE, name + " is now online.");  //send notification to all users that current user is online
                    break;
                 case LOGOUT_ACTION:
                    name = (String) (session.getUserProperties().get("name"));
                    Logger.getLogger(IPTPresentationEndpoint.class.getName()).log(Level.INFO, name 
                        + " has successfully logged out as "
                        + session.getUserProperties().get("userKind"));
                    session.getBasicRemote().sendObject(
                        new PresentationMessage(LOGOUT_RESPONSE, session.getId(), 
                            Json.createObjectBuilder().add("message", "You have successfully logged out.").build()));
                    sendMessageToOthers(session, USER_OFFLINE_RESPONSE, name + " is offline.");  //send notification to all users that current user is online
                    break;
            }
        } catch (EncodeException | IOException e ){
            try {
                session.getBasicRemote().sendObject(
                        new PresentationMessage(ERROR_RESPONSE, session.getId(),
                                Json.createObjectBuilder().add("message", e.getMessage()).build()));
                Logger.getLogger(IPTPresentationEndpoint.class.getName()).log(Level.SEVERE, null, e);
            } catch (IOException ex) {
                Logger.getLogger(IPTPresentationEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EncodeException ex) {
                Logger.getLogger(IPTPresentationEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** 
     * This method broadcasts messages to other logged users
     * @param currentSession the WebSocket session
     * @param messageType the type of message to be broadcasted
     * @param message the serialized message to be broadcasted 
     * @throws EncodeException exception generated when encoding not successful
     * @throws IOException 
     */
    private void sendMessageToOthers(Session currentSession, String messageType, String message) throws EncodeException, IOException {
        for(Session s: sessions){
            if(!s.getId().equals(currentSession.getId())){
                s.getBasicRemote().sendObject(
                    new PresentationMessage(messageType, s.getId(), 
                        Json.createObjectBuilder().add("message", message).build()));
            }
        }
    }
}
