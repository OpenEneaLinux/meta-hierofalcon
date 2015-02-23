SECTION = "kernel"
DESCRIPTION = "Linux kernel for AMD Hierofalcon Corttex-A57"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "hierofalcon"

inherit kernel siteinfo

MACHINE_KERNEL_PR_append = "b+gitr${SRCPV}"
PR = "${MACHINE_KERNEL_PR}"

SRCREV = "bfa76d49576599a4b9f9b7a71f23d73d6dcff735"
SRC_URI = "git://git.fedorahosted.org/git/kernel-arm64.git;branch="devel";protocol=git \
           file://01-arm64-boot-BE-kernels-from-UEFI.patch \
           file://02-Hierofalcon-Enable-32-bit-EL0-with-64K-and-4K-page-s.patch \
           file://03-arm64-psci-tell-the-compiler-which-registers-we-are-.patch \
           file://04-Add-support-for-A0-silicon-xgbe-driver.patch \
           file://05-Wire-in-support-for-poweroff-via-UEFI.patch \
           file://06-arm64-don-t-set-READ_IMPLIES_EXEC-for-EM_AARCH64-ELF.patch \
           file://amdconfig \
           file://defconfig \
           "

DEPENDS += "libgcc"

S = "${WORKDIR}/git"

KERNEL_LOCALVERSION ?= ""
KERNEL_EXTRA_ARGS += "dtbs"

addtask setup_defconfig before do_configure after do_patch

KBIGEND = ""
KBIGEND_aarch64-be = "CONFIG_CPU_BIG_ENDIAN=y"
do_setup_defconfig() {
    echo "${KBIGEND}" >> ${WORKDIR}/amdconfig

    cp ${WORKDIR}/amdconfig ${S}/arch/arm64/configs/amd_defconfig
    cp ${WORKDIR}/defconfig ${S}/.config
}

do_configure() {
    config=`cat ${S}/.config | grep use-kernel-config | cut -d= -f2`
    if [ "x${config}" != "x" ]
    then
        oe_runmake ${config}
    else
        yes '' | oe_runmake oldconfig
    fi
}

do_deploy_append() {
    install -m 0644 ${B}/arch/arm64/boot/dts/amd/amd-overdrive.dtb ${DEPLOYDIR}/${KERNEL_IMAGE_BASE_NAME}.dtb
}

INSANE_SKIP_${PN} += "installed-vs-shipped"
INSANE_SKIP_${PN} += "debug-files"
INSANE_SKIP_${PN} += "arch"
