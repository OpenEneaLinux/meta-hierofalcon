SECTION = "kernel"
DESCRIPTION = "Linux kernel for AMD Hierofalcon Corttex-A57"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "hierofalcon"

inherit kernel siteinfo

MACHINE_KERNEL_PR_append = "b+gitr${SRCPV}"
PR = "${MACHINE_KERNEL_PR}"

SRCREV = "6713ff4d26ff95ba1f5491eba6d624557831498e"
SRC_URI = "git://git.fedorahosted.org/git/kernel-arm64.git;branch="devel";protocol=git \
           file://amdconfig \
           file://defconfig \
           "

DEPENDS += "libgcc"

S = "${WORKDIR}/git"

KERNEL_LOCALVERSION ?= ""
KERNEL_EXTRA_ARGS += "dtbs"

addtask setup_defconfig before do_configure after do_patch
do_setup_defconfig() {
    cp ${WORKDIR}/amdconfig ${S}/arch/arm64/configs/amd_defconfig
    cp ${WORKDIR}/defconfig ${S}/.config
}

do_configure() {
    echo ${KERNEL_LOCALVERSION} > ${B}/.scmversion
    echo ${KERNEL_LOCALVERSION} > ${S}/.scmversion
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
