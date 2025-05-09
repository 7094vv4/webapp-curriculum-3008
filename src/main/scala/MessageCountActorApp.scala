import akka.actor.{Actor, ActorSystem, Props}
import scala.concurrent.Await
import scala.concurrent.duration._

class MessageCountActor extends Actor {
  var count = 0

  def receive: Receive = {
    case n: Int =>
      count += 1
      println(s"Count: $n")
      if (n >= 10000) {
        println("Finished counting. Shutting down system.")
        context.system.terminate()
      }
  }
}

object MessageCountActorApp extends App {
  val system = ActorSystem("MessageCountSystem")
  val counter = system.actorOf(Props[MessageCountActor], "counterActor")

  for (i <- 1 to 10000) {
    counter ! i
  }

  Await.result(system.whenTerminated, Duration.Inf)
}

