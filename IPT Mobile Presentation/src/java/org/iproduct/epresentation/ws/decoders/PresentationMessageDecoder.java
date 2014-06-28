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

package org.iproduct.epresentation.ws.decoders;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import org.iproduct.epresentation.ws.message.PresentationMessage;
import static org.iproduct.epresentation.ws.message.PresentationMessage.LOGIN_ACTION;
import static org.iproduct.epresentation.ws.message.PresentationMessage.LOGOUT_ACTION;
import static org.iproduct.epresentation.ws.message.PresentationMessage.NEW_SLIDE_ACTION;
import static org.iproduct.epresentation.ws.message.PresentationMessage.WATCH_ACTION;

/**
 * This class represents {@link org.iproduct.epresentation.ws.message.PresentationMessage PresentationMessage}
 * decoder from JSON String received  
 *
 * @author Trayan Iliev
 */
public class PresentationMessageDecoder implements Decoder.Text<PresentationMessage> {

    @Override
    public PresentationMessage decode(String jsonData) throws DecodeException {
        JsonObject obj;
        PresentationMessage message = null;
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonData))) {
            obj = jsonReader.readObject();
            if (obj.containsKey("type") && obj.containsKey("sid") && obj.containsKey("data")) {
                message = new PresentationMessage(
                        obj.getString("type"), obj.getString("sid"), obj.getJsonObject("data"));
            } else {
                throw new DecodeException(jsonData, "Invalid json string");
            }
        }
        return message;
    }

    @Override
    public boolean willDecode(String jsonData) {
        JsonObject obj;
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonData))) {
            obj = jsonReader.readObject();
            if (obj.containsKey("type") && obj.getString("type").length() > 0) {
                String type = obj.getString("type");
                switch (type) {
                    case LOGIN_ACTION:
                    case LOGOUT_ACTION:
                    case WATCH_ACTION:
                    case NEW_SLIDE_ACTION:
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
