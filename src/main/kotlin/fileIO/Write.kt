package fileIO

import java.io.File

//TODO: this would write to S3
fun writeSpec(specName: String, input: String) = File("src/main/resources/$specName.json").writeText(input)