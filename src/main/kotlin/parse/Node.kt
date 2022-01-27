package parse

class Node(val type: NodeType, val number:Int? = null, val ident:String? = null){

    val children = mutableListOf<Node>()

    fun children(vararg childList:Node): Node {
        children.addAll(childList)
        return this
    }
}