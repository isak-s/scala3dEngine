trait Vector3 {
    var x: Double
    var y: Double
    var z: Double
    infix def cross(o: Vector3): Vertex4 = Vertex4(y * o.z - z * o.y, z * o.x - x * o.z, x * o.y - y * o.x, 0) // 0 since it is a direction

    infix def dot(o: Vector3): Double = x * o.x + y * o.y + z* o.z

    infix def +(v: Vector3): Vertex4 = Vertex4(x + v.x, y + v.y, z + v.z, 0)

    infix def -(v: Vector3): Vertex4 = Vertex4(x - v.x, y - v.y, z - v.z, 0)

    infix def *(s: Double): Vertex4 = Vertex4(x * s, y * s, z * s, 0)

    def magnitude: Double = math.sqrt(this dot this)

    def normalize: Vertex4 = this * (1.0 / magnitude)
}

// case class Vertex3(var x: Double, var y: Double, var z: Double) extends Vector3

case class Vertex4(var x: Double, var y: Double, var z: Double, var w: Double) extends Vector3

object Vertex4 {
    def midPoint(vs: Vertex4*): Vertex4 = {
        require(vs.nonEmpty, "Cannot compute midpoint of an empty sequence of vertices.")
        val result = Array(0.0, 0.0, 0.0)
        vs.foreach {v =>
            result.update(0, result(0) + v.x);
            result.update(1, result(1) + v.y);
            result.update(2, result(2) + v.z);}

        val n = vs.length
        Vertex4(result(0) / n, result(1) / n, result(2) / n, 1)
    }
}
