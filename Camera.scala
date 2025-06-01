case class Camera(
    var position: Vertex4,
    var pitch: Double,
    var yaw: Double,
    var roll: Double = 0.0,

    var fov: Double = 60.0,  // degrees
    var aspectRatio: Double = 1.0,  // width/height
    var near: Double = 0.1,
    var far: Double = 1000.0
) {
    def forward: Vertex4 = {
        // Calculate forward vector from pitch and yaw
        val cosPitch = math.cos(math.toRadians(pitch))
        val sinPitch = math.sin(math.toRadians(pitch))
        val cosYaw = math.cos(math.toRadians(yaw))
        val sinYaw = math.sin(math.toRadians(yaw))

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
}