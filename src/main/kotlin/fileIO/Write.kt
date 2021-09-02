package fileIO

import java.io.File

// This would be replaced with a write to S3
fun writeSpec(specName: String, input: String) = File("src/main/resources/$specName.json").writeText(input)