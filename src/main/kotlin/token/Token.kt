package token

sealed class Token {
    var next:Token? = null

    fun next(next:Token):Token {
        this.next = next
        return next
    }

    class SOF : Token() {
        override fun toString(): String {
            return "[SOF] -> $next"
        }
    }
    class Reserved(val key:String): Token() {
        override fun toString(): String {
            return "[Reserved:${key}] -> $next"
        }
    }
    class Ident(val ident:String): Token() {
        override fun toString(): String {
            return "[Ident:${ident}] -> $next"
        }
    }
    class Number(val number:Int): Token() {
        override fun toString(): String {
            return "[${number}] -> $next"
        }
    }
    object EOF : Token() {
        override fun toString(): String {
            return "[EOF]"
        }
    }
}