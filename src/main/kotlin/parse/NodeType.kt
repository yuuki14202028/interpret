package parse

enum class NodeType {
    Program,
    Add,
    Sub,
    Mul,
    Div,
    Mod,
    Equal,
    NotEqual,
    GreaterOrEqual,//>=
    GreaterThan,//>
    LessThan,//<
    LessOrEqual,//<=
    ASSIGN,
    Var,
    Num,
    Fun,
}