package org.jetbrains.bsp.bazel.server.sync

import io.kotest.matchers.shouldBe
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import org.jetbrains.bsp.bazel.logger.BspClientLogger
import org.jetbrains.bsp.bazel.server.sync.languages.java.JavaModule
import org.jetbrains.bsp.bazel.server.sync.languages.java.Jdk
import org.jetbrains.bsp.bazel.server.sync.languages.scala.ScalaModule
import org.jetbrains.bsp.bazel.server.sync.languages.scala.ScalaSdk
import org.jetbrains.bsp.bazel.server.sync.model.Label
import org.jetbrains.bsp.bazel.server.sync.model.Language
import org.jetbrains.bsp.bazel.server.sync.model.Module
import org.jetbrains.bsp.bazel.server.sync.model.Project
import org.jetbrains.bsp.bazel.server.sync.model.SourceSet
import org.jetbrains.bsp.bazel.server.sync.model.Tag
import org.jetbrains.bsp.bazel.utils.dope.DopeTemp.createTempPath
import org.junit.jupiter.api.Test

class ProjectStorageTest {
    @Test
    @Throws(IOException::class)
    fun `should store and load project`() {
        val path = createTempPath("project-cache-test.json")
        Files.deleteIfExists(path)
        val storage = FileProjectStorage(path, BspClientLogger())
        val empty = storage.load()
        empty shouldBe null
        val scalaModule = ScalaModule(
            ScalaSdk("org.scala", "2.12.3", "2.12", emptyList()), emptyList(), JavaModule(
                Jdk("8", null),
                null,
                emptyList(),
                emptyList(),
                URI.create("file:///tmp/out"),
                emptyList(),
                null,
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            )
        )
        val project = Project(
            URI.create("file:///root"),
            listOf(
                Module(
                    Label("//project:project"),
                    false,
                    listOf(Label("//project:dep")),
                    hashSetOf(Language.JAVA),
                    hashSetOf(Tag.LIBRARY),
                    URI.create("file:///root/project"),
                    SourceSet(
                        hashSetOf(URI.create("file:///root/project/Lib.java")),
                        hashSetOf(URI.create("file:///root/project/"))
                    ),
                    emptySet(),
                    emptySet(),
                    scalaModule,
                    hashMapOf()
                )
            ),
            mapOf(URI.create("file:///root/project/Lib.java") to Label("file:///root")),
        )
        storage.store(project)
        val loaded = storage.load()
        loaded shouldBe project
    }
}
