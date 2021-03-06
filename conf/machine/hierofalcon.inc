#@TYPE: Machine
#@NAME: AMD Hierofalcon
#@DESCRIPTION: Cortex-A57

require conf/machine/include/arch-armv8.inc

PREFERRED_PROVIDER_virtual/kernel ?= "linux-hierofalcon"

MACHINE_FEATURES = "pci ext2 ext3 serial"

RDEPENDS_kernel-base = "kernel-image"

KERNEL_IMAGETYPE = "Image"

SERIAL_CONSOLES = "115200;ttyAMA0"

# we do not want to have getty running since  we do not have a
# graphical display attached and therefore do not need virtual
# terminal functionality
USE_VT = "0"
