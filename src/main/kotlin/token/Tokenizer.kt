package token

import java.lang.Exception
import java.lang.StringBuilder

class Tokenizer(val code:String) {

    var index = 0
    private val start = Token.SOF()

    private fun exist():Boolean {
        return index < code.length
    }

    private fun lead(dif:Int = 0):Char {
        if (code.length <= index + dif) { return ' ' }
        return code.getOrNull(index + dif) ?: throw Exception("Index exceeds code length")
    }

    private fun lead(dif:IntRange):String {
        if (code.length <= index + dif.last) { return "" }
        return code.substring(index+dif.first..index+dif.last) ?: throw Exception("Index exceeds code length")
    }

    private fun number():Int {
        var i = 0
        val numString = StringBuilder()
        while (code.getOrNull(index+i)?.isDigit() == true) {
            numString.append(code[index+i])
            i += 1
        }
        index += i-1
        return numString.toString().toInt()
    }

    private fun ident():String {
        var i = 0
        val indet = StringBuilder()
        while (code.getOrNull(index+i)?.isIndentable() == true) {
            indet.append(code[index+i])
            i += 1
        }
        index += i-1
        return indet.toString()
    }

    private fun Char.isIndentable():Boolean {
        return (this in 'a'..'z') or (this in 'A'..'Z')
    }

    fun tokenize():Token {
        var head:Token = start
        while (exist()) {
            val lead = lead()
            if (lead.isWhitespace()) {
                index += 1
                continue
            } else if ("fun" == lead(0..2)) {
                head = head.next(Token.Reserved("fun"))
                index += 3
                continue
            }else if (("><!=".contains(lead)) and (lead(1) == '=')) {
                head = head.next(Token.Reserved("${lead}${lead(1)}"))
                index += 2
                continue
            } else if ("+-*/()<>=!%;{}".contains(lead)) {
                head = head.next(Token.Reserved(lead.toString()))
                index += 1
                continue
            } else if (lead.isIndentable()) {
                val ident = ident()
                head = head.next(Token.Ident(ident))
                index += 1
                continue
            } else if (lead.isDigit()) {
                val num = number()
                head = head.next(Token.Number(num))
                index += 1
                continue
            }
            throw Exception("not validity token.")
        }
        head.next(Token.EOF)
        return start
    }
}