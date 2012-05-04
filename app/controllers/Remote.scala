package controllers

import play.api._
import play.api.mvc._
import play.api.libs.ws.WS

object RemoteWS extends Controller 
{	
    def twitter = Action {
        val url = "http://api.twitter.com/1/statuses/user_timeline.json?screen_name=mostlygeek"
        val start = System.currentTimeMillis()
        Async {
             WS.url(url).get().map { response =>
                 Ok("Got response, took: " + (System.currentTimeMillis() - start) + "ms\n" + response.body)
             }
        }       
    }
}