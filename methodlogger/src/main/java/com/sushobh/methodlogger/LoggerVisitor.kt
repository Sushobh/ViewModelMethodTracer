package com.sushobh.methodlogger

import org.objectweb.asm.*

class ViewModelMethodLoggerVisitor(
    classVisitor: ClassVisitor
) : ClassVisitor(Opcodes.ASM9, classVisitor) {

    private var isViewModel = false
    private lateinit var className: String

    override fun visit(
        version: Int, access: Int, name: String?, signature: String?,
        superName: String?, interfaces: Array<out String>?
    ) {
        className = name ?: ""
        isViewModel = superName == "androidx/lifecycle/ViewModel"
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (!isViewModel || name == "<init>" || name == "<clinit>" || (access and Opcodes.ACC_PUBLIC == 0)) {
            return mv
        }

        return object : MethodVisitor(Opcodes.ASM9, mv) {
            override fun visitCode() {
                super.visitCode()
                visitLdcInsn("VM")
                visitLdcInsn("ViewModelMethodLogger $className.$name called")
                visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "android/util/Log",
                    "d",
                    "(Ljava/lang/String;Ljava/lang/String;)I",
                    false
                )
                visitInsn(Opcodes.POP)
            }
        }
    }
}
