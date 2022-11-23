import java.io.File
import java.lang.StringBuilder
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class Json(private val cl: Any) {

    fun parseToJson(): String {
        val listOfParams: Array<String> = parseParams()
        val json = StringBuilder("{\n")
        var i = 0
        for (param in listOfParams) {
            json.append("   \"$param\":")
            val fieldValue = ((cl::class as KClass<in Any>).memberProperties.elementAt(i).also { it.isAccessible = true }.getter(cl)).toString()
            if ((cl::class as KClass<in Any>).memberProperties.elementAt(i).returnType.toString() == "kotlin.String") {
                json.append("\"$fieldValue\"")
            } else {
                json.append(fieldValue)
            }
            if (i < listOfParams.size - 1) {
                json.append(",")
            }
            json.append("\n")
            i += 1
        }
        return "$json}"
    }

    fun parseParams(): Array<String> {
        var params: Array<String> = arrayOf()
        for (j in cl::class.memberProperties) {
            val x = j.toString()
            val name = x.subSequence(x.indexOf(".") + 1, x.indexOf(":"))
            params+=name.toString()
        }
        return params
    }

    fun saveToFile() {
        val file = File("${cl::class.simpleName.toString()}.json")
        file.writeText(parseToJson())
    }
}