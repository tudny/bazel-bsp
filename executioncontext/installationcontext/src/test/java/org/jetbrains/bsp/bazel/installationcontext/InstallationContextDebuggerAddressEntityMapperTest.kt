package org.jetbrains.bsp.bazel.installationcontext

import io.kotest.matchers.shouldBe
import org.jetbrains.bsp.bazel.projectview.model.ProjectView
import org.jetbrains.bsp.bazel.projectview.model.sections.ProjectViewDebuggerAddressSection
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InstallationContextDebuggerAddressEntityMapperTest {

    @Nested
    @DisplayName("fun map(projectView: ProjectView): Try<InstallationContextDebuggerAddressEntity?> tests")
    inner class MapTest {

        @Test
        fun `should return success with empty debugger address from project view if debugger address is not specified in project view`() {
            // given
            val projectView = ProjectView.Builder(debuggerAddress = null).build().get()

            // when
            val debuggerAddressTry = InstallationContextDebuggerAddressEntityMapper.map(projectView)

            // then
            debuggerAddressTry.isSuccess shouldBe true
            val debuggerAddressOption = debuggerAddressTry.get()

            debuggerAddressOption shouldBe null
        }

        @Test
        fun `should return success with debugger address from project view if debugger address is specified in project view`() {
            // given
            val projectView =
                ProjectView.Builder(
                    debuggerAddress = ProjectViewDebuggerAddressSection("host:8000")
                ).build().get()

            // when
            val debuggerAddressTry = InstallationContextDebuggerAddressEntityMapper.map(projectView)

            // then
            debuggerAddressTry.isSuccess shouldBe true
            val debuggerAddressOption = debuggerAddressTry.get()

            val expectedDebuggerAddress = InstallationContextDebuggerAddressEntity("host:8000")
            debuggerAddressOption shouldBe expectedDebuggerAddress
        }
    }

    @Nested
    @DisplayName("fun default(): Try<InstallationContextDebuggerAddressEntity?>")
    inner class DefaultTest {

        @Test
        fun `should return success with empty debugger address`() {
            // given
            // when
            val debuggerAddressTry = InstallationContextDebuggerAddressEntityMapper.default()

            // then
            debuggerAddressTry.isSuccess shouldBe true
            val debuggerAddressOption = debuggerAddressTry.get()

            debuggerAddressOption shouldBe null
        }
    }
}
