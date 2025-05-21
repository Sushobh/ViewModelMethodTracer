package com.sushobh.loggerplugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

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
                visitLdcInsn(className) // First parameter
                visitLdcInsn("$name called") // Second parameter
                visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "com/sushobh/methodlogger2/LogReceiverKt", // top-level file name becomes class
                    "onMethodLogged", // function name
                    "(Ljava/lang/String;Ljava/lang/String;)V", // descriptor: two Strings, returns void
                    false
                )
            }
        }
    }
}
