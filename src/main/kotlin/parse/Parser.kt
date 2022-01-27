package parse

import token.Token

class Parser(private val token:Token){

    var head: Token = token

    fun program():Node {
        val ret = Node(NodeType.Program)
        while (head.next != Token.EOF) {
            ret.children.add(sentence())
        }
        return ret
    }

    private fun sentence(): Node {
        if (consume("fun")) {
            ident()?.let { ident ->
                consume("(")
                consume(")")
                consume("{")
                val block = Node(NodeType.Fun,null,ident)
                while (consume("}").not()) {
                    block.children(expr())
                }
                return block
            }
        }
        return expr()
    }

    private fun expr():Node {
        return assign()
    }

    private fun assign():Node {
        var node = equal()
        if (consume("=")) {
            node = Node(NodeType.ASSIGN).children(node,equal())
        }
        return node
    }

    private fun equal():Node {
        var node = relational()
        while (true) {
            node = if (consume("==")) {
                Node(NodeType.Equal).children(node,relational())
            } else if (consume("!=")) {
                Node(NodeType.NotEqual).children(node,relational())
            } else return node
        }
    }

    private fun relational():Node {
        var node = add()
        while (true) {
            node = if (consume("<")) {
                Node(NodeType.LessThan).children(node,add())
            } else if (consume("<=")) {
                Node(NodeType.LessOrEqual).children(node,add())
            } else if (consume(">")) {
                Node(NodeType.GreaterThan).children(node,add())
            } else if (consume(">=")) {
                Node(NodeType.GreaterOrEqual).children(node,add())
            } else return node
        }
    }

    private fun add():Node {
        var node = mul()
        while (true) {
            node = if (consume("+")) {
                Node(NodeType.Add).children(node,mul())
            } else if (consume("-")) {
                Node(NodeType.Sub).children(node,mul())
            } else return node
        }
    }

    private fun mul():Node {
        var node = unary()
        while (true) {
            node = if (consume("*")) {
                Node(NodeType.Mul).children(node,unary())
            } else if (consume("/")) {
                Node(NodeType.Div).children(node,unary())
            } else return node
        }
    }

    private fun unary():Node {
        if (consume("+")) {
            return primary()
        } else if (consume("-")) {
            return Node(NodeType.Sub).children(Node(NodeType.Num,0),primary())
        }
        return primary()

    }

    private fun primary():Node {
        if (consume("(")) {
            val node = expr()
            consume(")")
            return node
        }
        number()?.let { return Node(NodeType.Num,it,null) }
        ident()?.let { return Node(NodeType.Var,null,it) }
        throw Exception("The expected token was not found.${head}")
    }



    private fun consume(key:String):Boolean {
        val next = head.next
        if (next is Token.Reserved) {
            if (next.key == key) {
                head = next
                return true
            }
        }
        return false
    }

    private fun expect(key:String):Boolean {
        val next = head.next
        if (next is Token.Reserved) {
            if (next.key == key) {
                head = next
                return true
            }
        }
        throw Exception("The expected token $key was not found.")
    }

    private fun ident():String? {
        val next = head.next
        if (next is Token.Ident) {
            head = next
            return next.ident
        }
        return null
    }

    private fun number():Int? {
        val next = head.next
        if (next is Token.Number) {
            head = next
            return next.number
        }
        return null
    }
}