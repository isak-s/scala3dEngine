case class Vertex(x: Double, y: Double, z: Double) {
    infix def cross(o: Vertex): Vertex = Vertex(y * o.z - z * o.y, z * o.x - x * o.z, x * o.y - y * o.x)

    infix def dot(o: Vertex): Double = x * o.x + y * o.y + z* o.z

    infix def +(v: Vertex): Vertex = Vertex(x + v.x, y + v.y, z + v.z)

    infix def -(v: Vertex): Vertex = Vertex(x - v.x, y - v.y, z - v.z)

    infix def *(s: Double): Vertex = Vertex(x * s, y * s, z * s)

    def magnitude: Double = math.sqrt(this dot this)

    def normalize: Vertex = this * (1.0 / magnitude)
}