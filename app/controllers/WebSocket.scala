package controllers

import java.util.Date
import play.api._
import play.api.mvc._
import play.api.libs.iteratee.{Iteratee, Enumerator} // they are here :)
import play.api.libs.ws.WS
import play.api.libs.concurrent._

// point browser @ http://www.websocket.org/echo.html to test it out
// http://www.playframework.org/documentation/2.0.1/Iteratees
object WebSocket extends Controller {

    def endpoint = play.api.mvc.WebSocket.using[String] { request => 
       
        /**
         * This will create an Enumerator that prints out the 
         * time every second... 
         */
        val out = Enumerator.fromCallback { () =>
        	Promise.timeout(Some(new Date().toString()), 1000)
    	}
        
        val in = Iteratee.foreach[String] ( s => {                        
            println(s)
        })
        
        in.mapDone { _ =>
            println("Disconnected")
        }
        
        (in, out)        
    }

}
