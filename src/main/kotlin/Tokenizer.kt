import token.Token
import java.lang.Exception

class Tokenizer(val code:String) {

    var index = 0
    val start = Token.SOF()

    fun exist():Boolean {
        return index < code.length
    }

    fun lead():Char {
        return code.getOrNull(index) ?: throw Exception("Index exceeds code length")
    }

    fun tokenize():Token {
        var head = start
        while (exist()) {
            val lead = lead()
            if (lead.isWhitespace()) {
                index += 1
                continue
            }
        }

        return start
    }

}