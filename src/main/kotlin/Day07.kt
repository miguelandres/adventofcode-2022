import java.io.File

abstract class FileSystemNode(val name: String, val parent: Directory?) {

    abstract fun size(): Int
    abstract fun fullPath(): String
}

class Directory(name: String, parent: Directory?, var contents: HashMap<String, FileSystemNode> = hashMapOf()) :
    FileSystemNode(name, parent) {
    private var calculatedSize: Int? = null

    override fun size(): Int {
        return if (calculatedSize != null) {
            calculatedSize as Int
        } else {
            calculatedSize = contents.map { it.value.size() }.sum()
            calculatedSize!!
        }
    }

    override fun fullPath(): String {
        return (parent?.fullPath() ?: "") + name + "/"
    }
}

class FileNode(name: String, parent: Directory?, private val size: Int) : FileSystemNode(name, parent) {
    override fun size(): Int {
        return size
    }

    override fun fullPath(): String {
        return (parent?.fullPath() ?: "") + name
    }
}

fun parseFileOrDir(line: String, parent: Directory): FileSystemNode {
    val parts = line.split(" ")
    return if (parts[0] == "dir") {
        Directory(parts[1], parent)
    } else {
        FileNode(parts[1], parent, parts[0].toInt())
    }
}

fun calculateSizes(cwd: Directory): Map<String, Pair<Directory, Int>> {

    val mapSubDirs = cwd.contents.map { it.value }.filterIsInstance<Directory>().map { dir -> calculateSizes(dir) }
        .fold(mapOf<String, Pair<Directory, Int>>()) { a, b ->
            a.plus(b)
        }

    return mapSubDirs.plus(Pair(cwd.fullPath(), Pair(cwd, cwd.size())))
}

fun main() {
    val lines = File("input/d07p01.txt").readLines()
    val root = Directory("", null)
    var cwd = root
    for (line in lines) {
        if (line == "$ cd /") {
            cwd = root
        } else if (line == "$ cd ..") {
            cwd = cwd.parent!!
        } else if (line.startsWith("$ cd ")) {
            val subdir = line.substring(5, line.length)
            cwd = cwd.contents[subdir] as Directory
        } else if (line == "$ ls") {
            //
        } else {
            val node = parseFileOrDir(line, cwd)
            cwd.contents.putIfAbsent(node.name, node)
        }
    }

    val mapSizes = calculateSizes(root)

    val directoriesBySize = mapSizes.map { Triple(it.key, it.value.first, it.value.second) }.sortedBy { it.third }
    println(directoriesBySize.map { Pair(it.first, it.third) })
    val smallDirs = directoriesBySize.takeWhile { it.third <= 100000 }

    println(smallDirs.map { Pair(it.first, it.third) })
    println(smallDirs.sumOf { it.third })


    val maxSpace = 70000000
    val needed = 30000000
    val available = maxSpace - root.size()
    val needToDelete = needed - available

    println(directoriesBySize.find { it.third >= needToDelete }?.third)
}