fork in run := true

val os = System.getProperty("os.name").split(" ")(0).toLowerCase

val libPath = Seq(s"lib/native/$os").mkString(java.io.File.pathSeparator)

javaOptions in run += s"-Djava.library.path=$libPath"
