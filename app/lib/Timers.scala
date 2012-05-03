package lib

// ref: https://github.com/mostlygeek/Scala-Hacking/blob/master/src/Misc/ActorsForTimeouts.scala
import actors.{Actor, TIMEOUT}
object Timers {
	def in(milliseconds:Long)(f: => Unit) = {
		Actor.actor({
			Actor.reactWithin(milliseconds) {
			case TIMEOUT => f;
			case 'abort =>
			}	    
		})    
	}
}