package token

sealed class Token {
    var next:Token? = null

    fun next(next:Token):Token = also {
        it.next = next
    }

    class SOF : Token() {
    }
    class Reserved(val key:String) : Token() {
    }
    class Number(val number:Int) : Token() {
    }
    object EOF : Token() {

    }
}