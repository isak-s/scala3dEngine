import java.util.ArrayList

sealed trait Light

case class DirectionalLight(direction: Vertex4, brightness: Double) extends Light
case class PointLight(position: Vertex4, brightness: Double) extends Light