case class Camera(
    var position: Vertex4,
    var fovY: Double = Camera.degToRad(58),
    var aspectRatio: Double = 16 / 9,  // width/height
    var near: Double = 0.1,
    var far: Double = 1000.0
)(using angles: Angles) {

    def forward: Vertex4 = {
        // Calculate forward vector from pitch and yaw
        val cosPitch = math.cos(math.toRadians(angles.pitch))
        val sinPitch = math.sin(math.toRadians(angles.pitch))
        val cosYaw = math.cos(math.toRadians(angles.yaw))
        val sinYaw = math.sin(math.toRadians(angles.yaw))

        Vertex4(
            sinYaw * cosPitch,
            sinPitch,
            cosYaw * cosPitch,
            0
        ).normalize
  }

    def right: Vertex4 = {
        // Right vector is perpendicular to forward and global up (0,1,0)
        forward cross Vertex4(0, 1, 0, 0)
    }

    def up: Vertex4 = {
        right cross forward
    }

    def fovX: Double = {
        2 * math.atan(aspectRatio * math.tan(fovY / 2))
    }
}

object Camera {
    def degToRad(deg: Double): Double = deg * Math.PI / 180.0
    def radToDeg(rad: Double): Double = rad * 180.0 / Math.PI
}

case class Angles(var pitch: Double = 0.0, var yaw: Double = 0.0, var roll: Double = 0.0)