package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.{Iteratee, Enumerator} // they are here :)
import play.api.libs.ws.WS


// point browser @ http://www.websocket.org/echo.html to test it out
// http://www.playframework.org/documentation/2.0.1/Iteratees
object WebSocket extends Controller {

    def endpoint = play.api.mvc.WebSocket.using[String] { request => 
        val out = Enumerator("Hello!")
        
        val in = Iteratee.foreach[String] ( s => {                        
            println(s)
        })
        
        in.mapDone { _ =>
            println("Disconnected")
        }
        
        (in, out)        
    }

}
