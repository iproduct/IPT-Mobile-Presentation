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

package org.iproduct.epresentation.ws.message;

import javax.json.JsonObject;

/**
 * This class represents PresentationMessage structure and defines different
 * presentation massage types
 *
 * @author Trayan Iliev
 */
public class PresentationMessage{

    public static final String LOGIN_ACTION = "login";
    public static final String LOGIN_RESPONSE = "login-resp";
    public static final String LOGOUT_ACTION = "logout";
    public static final String LOGOUT_RESPONSE = "logout-resp";
    public static final String WATCH_ACTION = "watch";
    public static final String WATCH_RESPONSE = "watch-resp";
    public static final String NEW_SLIDE_ACTION = "new-slide";
    public static final String NEW_SLIDE_RESPONSE = "new-slide-resp";
    public static final String ERROR_RESPONSE = "error-resp";
    public static final String USER_ONLINE_RESPONSE = "online-resp";
    public static final String USER_OFFLINE_RESPONSE = "offline-resp";

    private final String type;

    private final String sessionId;
    
    private final JsonObject payload;
   
    public PresentationMessage(String type, String sessionId, JsonObject payload) {
        this.type = type;
        this.sessionId = sessionId;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public String getSessionId() {
        return sessionId;
    }

    public JsonObject getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "PresentationMessage{" + "type=" + type + ", userId=" + sessionId + ", payload=" + payload + '}';
    }  
}