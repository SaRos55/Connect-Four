fun solution(strings: List<String>, str: String): Int {
    var counter = 0
    for (string in strings) {
        if (string == str) counter++
    }
    return counter
}