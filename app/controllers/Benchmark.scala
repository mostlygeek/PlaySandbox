package controllers

import play.api._
import play.api.mvc._
import play.libs._
import play.api.libs._		 // mostly for the concurrent library
import play.api.Play.current // required for Akka.future

/**
 * This tests if Play is a singled thread process handling requests 
 * or if it is multi-threaded, handling each request separately. 
 * 
 * See below for the ab (apache bench) results. 
 * 
 */
object Benchmark extends Controller {
	
	val sleepTime:Long = 500
	
	def fast = Action {
	  Ok("Hi")
	}
	
	def block = Action {
	  val now = System.currentTimeMillis()
	  
	  Thread.sleep(sleepTime)
	    
	  Ok("Done, blocked for: " + sleepTime + "ms")
	}
	
	/**
	 * Ref: http://www.playframework.org/documentation/2.0/ScalaAsync
	 * 
	 * Play basically expects requests to finish as fast as possible, 
	 * like a single threaded application.
	 */
	
	def async = Action {
		Async {
		  val p1 : concurrent.Promise[Int] = concurrent.Akka.future {
		    Thread.sleep(sleepTime)
		    1
		  }
		  
		  p1.map { _ => Ok("I async slept for: " + sleepTime + "ms") }
		}
	}
}
