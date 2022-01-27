import gen.Generator
import parse.Parser
import token.Tokenizer

fun main(args: Array<String>) {
    println("Hello World!")

    val code = """
        fun a() {
            a = 5
            b = a*a
            c = b+a
            result = c/3
        }
        
        fun main() {
            a = 20
            main = 5*a
        }
    """.trimIndent()//10
    val token = Tokenizer(code).tokenize()
    val node = Parser(token).program()
    Generator(node).generate()
}