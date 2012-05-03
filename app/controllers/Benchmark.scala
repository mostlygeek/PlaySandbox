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
	
	def fast = Action {
	  Ok("Hi")
	}
	
	def block = Action {
	  val now = System.currentTimeMillis()
	  val sleepFor:Long = 1000
	  Thread.sleep(sleepFor)
	    
	  Ok("Done, blocked for: " + sleepFor + "ms")
	}
	
	/**
	 * Ref: http://www.playframework.org/documentation/2.0/ScalaAsync
	 * 
	 * Play basically expects requests to finish as fast as possible, 
	 * like a single threaded application.
	 */
	
	def async = Action {
		val sleepFor:Long = 1000
		Async {		  
		  val p1 : concurrent.Promise[Long] = concurrent.Akka.future {
		    // usually put a long running function here that returns a value
		    // when it is done... 
		    Thread.sleep(sleepFor) 
		    sleepFor
		    
		    /* using Thread.sleep is bad, it is good to use the Actor system
		     * maybe on a remote node to do the long processing operation ... 
		     * mmm
		     */
		  }
		  
		  p1.map { s => Ok("I async slept for: " + s + "ms") }
		}
	}
	
	/*
	 * Instead of a long running function we use Actors, see the lib/Timers.scala
	 * that will timeout in a specific amount of time and run our callback
	 * function
	 * 
	 * using reemTime of 1000ms, we limit requests to 1/second :)
	 * try bumping up the concurrency and it should be very concurrent and 
	 * slow :D
	 * 
	 * try: ab -c 50 -n 100  http://localhost:9000/benchmark/async2
	 * -- should take 2 seconds
	 * 
	 */
	def async2 = Action {
	  val redeemTime:Long = 1000 // ms
	  Async {
		  val prom = concurrent.Promise[Int]
		  lib.Timers.in(redeemTime) {
		    prom.redeem(1)
		  }
		  prom.map { _ => Ok("Reemed in: " + redeemTime + "ms") }
	  }
	}
}
