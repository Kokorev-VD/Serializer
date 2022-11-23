import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class R(val a: Int, var b: String)

class User(var id: Int, var login: String, var tags: List<Int>)

class Json(cl: Any) {

    private val cl: Any = cl
    private val fileName: String = "${cl::class.simpleName.toString()}.json"

    fun parseToJson(): String {
        val listOfParams: List<String> = parseProperies()
        var json: String = "{\n"
        var i = 0
        for (param in listOfParams) {
            if ((cl::class as KClass<in Any>).memberProperties.elementAt(i).returnType.toString() == "kotlin.String") {
                json += "   \"$param\":\"${
                    (cl::class as KClass<in Any>).memberProperties.elementAt(i)
                        .also { it.isAccessible = true }.getter(cl)}\""
            } else {
                json += "   \"$param\":${
                    (cl::class as KClass<in Any>).memberProperties.elementAt(i)
                        .also { it.isAccessible = true }.getter(cl)}"
            }
            if (i < listOfParams.size - 1) {
                json += ","
            }
            json += "\n"
            i += 1
        }
        return "$json}"
    }

    fun parseProperies(): MutableList<String> {
        val properties: MutableList<String> = mutableListOf()
        for (j in cl::class.memberProperties) {
            val x = j.toString()
            val name = x.subSequence(x.indexOf(".") + 1, x.indexOf(":"))
            properties.add(name.toString())
        }
        return properties
    }

    fun saveToFile() {
        val file = File(fileName)
        file.writeText(parseToJson())
    }

}

fun main() {
    val r = R(2, "ab")
    val user = User(0, "V", listOf(1, 2))
    val jsonR: Json = Json(r)
    jsonR.saveToFile()
    val jsonUser = Json(user)
    jsonUser.saveToFile()
}
