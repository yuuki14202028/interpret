package parse

class Selector {

    var calcCounter = -1

    var anonymousCounter = -1

    fun getRegister(address:String): RegisterType {
        return RegisterType.values().first { it.key == address }
    }

    fun math(): RegisterType {
        calcCounter += 1
        val register = getRegister("s${calcCounter}")
        return register
    }

    fun result(): RegisterType {
        val register = getRegister("s${calcCounter}")
        calcCounter -= 1
        return register
    }

    fun reset() {
        calcCounter = 0
    }

    fun anonymous():Int {
        anonymousCounter += 1
        return anonymousCounter
    }
}