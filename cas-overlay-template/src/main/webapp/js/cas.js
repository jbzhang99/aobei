/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var scripts = [ 
    "https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js",
    "https://lib.baomitu.com/jqueryui/1.10.3/jquery-ui.min.js",
    "https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js",
    "https://cdn.bootcss.com/javascript-debug/0.4/ba-debug.min.js",
    "https://cdn.bootcss.com/jquery-backstretch/2.0.4/jquery.backstretch.min.js",
    "https://cdnjs.cloudflare.com/ajax/libs/fingerprintjs2/1.6.1/fingerprint2.min.js"];

head.ready(document, function() {
    head.load(scripts, resourceLoadedSuccessfully);
});


function areCookiesEnabled() {
    $.cookie('cookiesEnabled', 'true');
    var value = $.cookie('cookiesEnabled');
    if (value != undefined) {
        $.removeCookie('cookiesEnabled');
        return true;
    }
    return false;
}

function resourceLoadedSuccessfully() {
    $(document).ready(function() {
        if ($(":focus").length === 0){
            $("input:visible:enabled:first").focus();
        }


        if (areCookiesEnabled()) {
           $('#cookiesDisabled').hide();
        } else {
           $('#cookiesDisabled').show();   
           $('#cookiesDisabled').animate({ backgroundColor: 'rgb(187,0,0)' }, 30).animate({ backgroundColor: 'rgb(255,238,221)' }, 500);
        }
            
        //flash error box
        $('#msg.errors').animate({ backgroundColor: 'rgb(187,0,0)' }, 30).animate({ backgroundColor: 'rgb(255,238,221)' }, 500);

        //flash success box
        $('#msg.success').animate({ backgroundColor: 'rgb(51,204,0)' }, 30).animate({ backgroundColor: 'rgb(221,255,170)' }, 500);

        //flash confirm box
        $('#msg.question').animate({ backgroundColor: 'rgb(51,204,0)' }, 30).animate({ backgroundColor: 'rgb(221,255,170)' }, 500);

        $('#capslock-on').hide();
        $('#password').keypress(function(e) {
            var s = String.fromCharCode( e.which );
            if ( s.toUpperCase() === s && s.toLowerCase() !== s && !e.shiftKey ) {
                $('#capslock-on').show();
            } else {
                $('#capslock-on').hide();
            }
        });

        if (typeof(jqueryReady) == "function") {
            jqueryReady();
        }
        //errors
        $('#username\\.errors,#password\\.errors').closest('.form-group').addClass('has-error');
        
        //init background slide images
        $.backstretch([
           './css/img/bg/1.jpg',
           './css/img/bg/2.jpg',
           './css/img/bg/3.jpg',
           './css/img/bg/4.jpg',
           ], {
             fade: 1000,
             duration: 8000
       		}
         );

        //set fingerprint
        new Fingerprint2().get(function(result, components) {
        	$('#Duuid').val(result);
        });
    });

};
