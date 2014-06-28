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

// remove default back button
jQuery(document).bind('mobileinit',function(){
    $.mobile.page.prototype.options.addBackBtn = false;
});

jQuery(document).ready(function($){
    /* Global attributes */
    var rootUri = "http://" + (document.location.hostname.length > 0 ? document.location.hostname: "localhost") 
        + ":" + (document.location.port.length > 0 ? document.location.port : "8080") + "/ipt-present";
    var rootWsUri = "ws://" + (document.location.hostname.length > 0 ? document.location.hostname: "localhost") 
        + ":" + (document.location.port.length > 0 ? document.location.port : "8080") + "/ipt-present/ws";
    var userName = "";   //the name of the currently logged user
    var sessionId = "";  //WebSocket session Id
    
    /* Web Socket Methods */
    var websocket = new WebSocket(rootWsUri);
    websocket.onopen = function (event) {
        onOpen();
    };
    websocket.onmessage = function (event) {
        onMessage(event)
    };
    websocket.onerror = function (event) {
        onError(event)
    };
    
    function onOpen(event) {       
    }
    
    function onMessage(event) {
        var jso = JSON.parse(event.data);
        switch(jso.type){
            case "login-resp": 
                sessionId = jso.sid;
                $(".logged-name").html(" - " + userName);
                $("#button-login").hide();
                $("#button-logout").show();
                showToster(jso.data.message, "info"); 
                break;
            case "logout-resp": 
                sessionId = "";
                userName = "";
                $(".logged-name").html("");
                $("#button-logout").hide();
                $("#button-login").show();
                showToster(jso.data.message, "info"); 
                break;
            case "online-resp": 
            case "offline-resp": 
                showToster(jso.data.message, "info"); 
                break;
            case "error-resp": 
                showToster(jso.data.message, "error"); 
                break;
        }
    }

    function onError(event) {
    }

    //attach logon handler
    $("#button-sign").on("tap", function(){
        var name = $("#popup-login #name").val();
        if(name.trim().length === 0){
            showToster("Name field should not be empty.", "error");
        } else {
            userName = name;
            var message = JSON.stringify({
                "type" : "login",
                "sid"  : "",
                "data" : { 
                    "name" : name
                }
            });
            websocket.send(message);
            $( "#popup-login" ).popup( "close" );
        }
    });
    
    //attach logout handler
    $("#button-logout").hide().on("tap", function(){
        var message = JSON.stringify({
            "type" : "logout",
            "sid"  : sessionId,
            "data" : {}
        });
        websocket.send(message);
    });   
    
    

    /*   Page Presentations   */
    var presentationsPage = $("#page-presentations");
    var presentationsList = $("#presentations-list");
    
    // load preesentations into presentations page list
    presentationsPage.bind("pagebeforeshow", function(){
        $.mobile.loading( "show", {
            text: "Loading presentations ...",
            textVisible: true,
            theme: "a",
            textonly: false
        });        
        function addPresentation(presentation) {
            var li = $("<li class='presentation'>" +
                "<a href='#page-slides' data-presentation-id='" + presentation.id + "'>" +
                "<h3 class='presentation-name'>" + presentation.name + "</h3>" + 
                "<p>" + presentation.description + "</p>" +  "</a></li>");
            presentationsList.append(li);
        }

        function showPresentations(presentations){
            $( "#presentations-list .presentation" ).remove();
            $(presentations).each( function(index,presentation){
                addPresentation(presentation);
            });
            presentationsList.listview("refresh", true);
            $.mobile.loading( "hide" );
           
            //bind event to each presentation link
            $("#page-presentations .presentation a").on("tap", function(){
                presentationId = $(this).attr("data-presentation-id");
                presentationName =  $(this).find(".presentation-name").html();
                $(".presentation-name-container").html(presentationName);
                getAllSlides(showSlides);
            });
        }
    
        function getAllPresentations(){
            $( "#users tbody" ).html("");
            $.ajax({
                accept: 'application/json',
                dataType: 'json',
                success: function(data, textStatus, xhr){
                    console.log(data);
                    showPresentations(data);
                    presentations = data;
                },
                error: function(xhr, textStatus, errorThrown){
                    $('#messages').hide();
                    $('#errors').html( 
                        "Error: Request was not successful!<br />" 
                        + xhr.status + " - " + textStatus + xhr.responseText).show();
                },
                type: 'GET',
                url: rootUri + "/rest/presentation"
            });
        }
        getAllPresentations();
    });  
    
    var slidesList = $("#slides-list");
    var presentationId;
    var presentationName;
    var listSlides;
    var currentSlideNumber = 0;
    
    function getAllSlides(callback){
        $.mobile.loading( "show", {
            text: "Loading slides ...",
            textVisible: true,
            theme: "a",
            textonly: false
        });        
        $.ajax({
            accept: 'application/json',
            dataType: 'json',
            success: function(data, textStatus, xhr){
                console.log(data);
                callback(data);
                presentations = data;
            },
            error: function(xhr, textStatus, errorThrown){
                $('#messages').hide();
                $('#errors').html( 
                    "Error: Request was not successful!<br />" 
                    + xhr.status + " - " + textStatus + xhr.responseText).show();
            },
            type: 'GET',
            url: rootUri + "/rest/" + presentationId + "/slide"
        });
    }

    function showSlides(slides){
        loadSlides(slides)
    }

   function loadSlides(slides){
        listSlides = slides;
        $( "#slides-list .slide" ).remove();
        $(slides).each( function(index, slide){
            addSlide(index, slide);
        });
        try{
            slidesList.listview("refresh", true);
        } catch(e){};
        $.mobile.loading( "hide" );
        $("#page-slides .slide a").on("tap", function(){
            slideNumber = parseInt($(this).attr("data-slide-number"));
            $(".slide-id-container").html(slideNumber + 1);

            //add detils
            $( "#slide-details-list li[data-role!='list-divider']" ).remove();
            addSlideAttribute('Caption', listSlides[slideNumber].caption);
            addSlideAttribute('Author', listSlides[slideNumber].author);
            addSlideAttribute('Audience',
                (listSlides[slideNumber].audience === 0)? 'All': listSlides[slideNumber].audience);
            addSlideAttribute('Duration', 
                (listSlides[slideNumber].duration === null)?'Not specified': listSlides[slideNumber].duration);
            addSlideAttribute('Created', new Date(listSlides[slideNumber].created));
            addSlideAttribute('Modified', new Date(listSlides[slideNumber].modified));
            addSlideAttribute('Description', listSlides[slideNumber].description);
            slideDetailsList.listview("refresh", true);
        });
   }
   
   function addSlide(index, slide) {
        var li = $("<li class='slide'>" +
            "<a href='#page-slide-details' data-slide-id='" + slide.id + 
            "' data-slide-number='" + index + 
            "' data-slide-url='" + slide.dataUrl + "'>" +
            "<h3>" + slide.caption + "</h3>" + 
            "<p>" + slide.dataUrl + "</p>" +  "</a></li>");
        slidesList.append(li);
    }


    /*   Page Slides   */
    var slideDetailsPage = $("#page-slide-details");
    var slideDetailsList = $("#slide-details-list");
    var slideNumber;
    
    function addSlideAttribute(name, value){
        var li = $("<li class='slide-attribute'>" +
            "<span class='slide-attribute-name'>" + name +
            "</span>: <span class='slide-attribute-value'>" + value +
            "</span></li>");
        slideDetailsList.append(li);
    }

    /*    Page Play     */
    var playHeader = $("#play-header");
    var playSlideContainer = $("#play-slide-container");
    $("#page-slides a#button-play").on("tap", function(){
        currentSlideNumber = 0;
        playSlideContainer.html("<img class='play-current-slide' src='" + rootUri +
            "/resources/" + listSlides[currentSlideNumber].dataUrl +"' />");
    });
    $("#page-play a#button-slide-previous").on("tap", function(){
        currentSlideNumber--;
        if(currentSlideNumber < 0)
            currentSlideNumber = 0;
       playHeader.html("ePresentation - " + (currentSlideNumber+1) + "/" + listSlides.length);
       playSlideContainer.html("<img class='play-current-slide' src='" + rootUri +
            "/resources/" + listSlides[currentSlideNumber].dataUrl +"' />");
    });
    $("#page-play a#button-slide-next").on("tap", function(){
        currentSlideNumber++;
        if(currentSlideNumber >= listSlides.length)
            currentSlideNumber = listSlides.length - 1;
        playHeader.html("ePresentation - " + (currentSlideNumber+1) + "/" + listSlides.length);
        playSlideContainer.html("<img class='play-current-slide' src='" + rootUri +
            "/resources/" + listSlides[currentSlideNumber].dataUrl +"' />");
    });
    $("#page-play a#button-slide-refresh").on("tap", function(){
        getAllSlides(loadSlides);
        if(currentSlideNumber < 0)
            currentSlideNumber = 0;
        if(currentSlideNumber >= listSlides.length)
            currentSlideNumber = listSlides.length - 1;
        playHeader.html("ePresentation - " + (currentSlideNumber+1) + "/" + listSlides.length);
        playSlideContainer.html("<img class='play-current-slide' src='" + rootUri +
            "/resources/" + listSlides[currentSlideNumber].dataUrl +"' />");
    });
    $("#page-play a#button-slide-first").on("tap", function(){
       currentSlideNumber = 0;
       playHeader.html("ePresentation - " + (currentSlideNumber+1) + "/" + listSlides.length);
       playSlideContainer.html("<img class='play-current-slide' src='" + rootUri +
            "/resources/" + listSlides[currentSlideNumber].dataUrl +"' />");
    });
    $("#page-play a#button-slide-last").on("tap", function(){
        currentSlideNumber = listSlides.length - 1;
        playHeader.html("ePresentation - " + (currentSlideNumber+1) + "/" + listSlides.length);
        playSlideContainer.html("<img class='play-current-slide' src='" + rootUri +
            "/resources/" + listSlides[currentSlideNumber].dataUrl +"' />");
    });

    function showToster(message, messageKind){
        $("<div class='ui-overlay-shadow ui-body-b ui-corner-all'>" + message + "</div>")
            .css({  display: "none",
                    opacity: 0.90,
                    position: "fixed",
                    padding: "20px 7px",
                    "text-align": "center",
                    width: "270px",
                    "word-wrap" : "break-word",
                    "z-index": 10000,
                    "background-color": (messageKind == "error")? "rgb(255,200,200)" : "rgb(220,255,200)",
                    "border": (messageKind == "error")? "1px solid rgb(255,150,150)" : "1px solid rgb(200,255,180)",
                    right: (($(window).width() - 284)/2 > 40) ? "40px": ($(window).width() - 284)/2 + "px",
                    bottom: "-1px"})
            .appendTo( $.mobile.pageContainer )
            .fadeIn( 400 )
            .delay( 6000 )
            .fadeOut( 700, function(){
                $(this).remove();
        });
    }

});

