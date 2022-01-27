package gen

import parse.Node
import parse.NodeType
import parse.Selector
import java.io.File


class Generator(val node:Node) {

    private var builder = StringBuilder()
    private val selector = Selector()

    fun generate() {
        gen(node)
    }

    fun switch(sub:StringBuilder? = null):StringBuilder {
        val ret = builder
        builder = sub ?: StringBuilder()
        return ret
    }

    private fun gen(node: Node) {
        when (node.type) {
            NodeType.Program -> {
                node.children.forEach {
                    gen(it)
                }
            }
            NodeType.Fun -> {
                val file = File("outputs/${node.ident}.mcfunction")
                val writer = file.writer()
                val main = switch()
                node.children.forEach {
                    gen(it)
                }
                val sub = switch(main)
                writer.write(sub.toString())
                writer.close()
            }
            NodeType.Num -> {
                builder.appendLine("scoreboard players set ${selector.math().key} register ${node.number}")
            }
            NodeType.Var -> {
                builder.appendLine("execute store result score ${selector.math().key} register run data get storage interpret:locals ${node.ident}")
            }
            NodeType.ASSIGN -> {
                gen(node.children[1])
                builder.appendLine("scoreboard players operation r0 register = ${selector.result().key} register")
                builder.appendLine("execute store result storage interpret:locals ${node.children[0].ident} int 1 run scoreboard players get r0 register")
            }
            NodeType.Add -> {
                binary(node)
                builder.appendLine("scoreboard players operation r0 register += r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.Sub -> {
                binary(node)
                builder.appendLine("scoreboard players operation r0 register -= r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.Mul -> {
                binary(node)
                builder.appendLine("scoreboard players operation r0 register *= r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.Div -> {
                binary(node)
                builder.appendLine("scoreboard players operation r0 register /= r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.Mod -> {
                binary(node)
                builder.appendLine("scoreboard players operation r0 register %= r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.Equal -> {
                binary(node)
                builder.appendLine("execute store result score r0 register run execute if score r0 register = r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.NotEqual -> {
                binary(node)
                builder.appendLine("execute store result score r0 register run execute unless score r0 register = r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.GreaterOrEqual -> {
                binary(node)
                builder.appendLine("execute store result score r0 register run execute if score r0 register >= r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.GreaterThan -> {
                binary(node)
                builder.appendLine("execute store result score r0 register run execute if score r0 register > r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.LessOrEqual -> {
                binary(node)
                builder.appendLine("execute store result score r0 register run execute if score r0 register <= r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
            NodeType.LessThan -> {
                binary(node)
                builder.appendLine("execute store result score r0 register run execute if score r0 register < r1 register")
                builder.appendLine("scoreboard players operation ${selector.math().key} register = r0 register")
            }
        }
    }

    fun binary(node: Node) {
        gen(node.children[0])
        gen(node.children[1])
        builder.appendLine("scoreboard players operation r1 register = ${selector.result().key} register")
        builder.appendLine("scoreboard players operation r0 register = ${selector.result().key} register")
    }
}