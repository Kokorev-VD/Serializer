fun main() {
    val r = R(2, "ab")
    val user = User(0, "V", listOf(1, 2))
    val jsonR = Json(r)
    jsonR.saveToFile()
    val jsonUser = Json(user)
    jsonUser.saveToFile()
}
