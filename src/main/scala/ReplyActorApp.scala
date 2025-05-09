import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

case class Message(context: String, senderRef: ActorRef)

class ReplyActor(name: String) extends Actor {
  def receive: Receive = {
    case Message(content, senderRef) =>
      println(s"$name received: $content")
      Thread.sleep(500)
      senderRef ! Message(s"Reply from $name", self)
  }
}

object ReplyActorApp extends App {
  val system = ActorSystem("ReplyActorSystem")

  val actor1 = system.actorOf(Props(new ReplyActor("Actor1")), "actor1")
  val actor2 = system.actorOf(Props(new ReplyActor("Actor2")), "actor2")

  actor1 ! Message("Hello from main!", actor2)

  println("Press ENTER to exit the program...")
  StdIn.readLine()
  Await.ready(system.terminate(), 5.seconds)
}

